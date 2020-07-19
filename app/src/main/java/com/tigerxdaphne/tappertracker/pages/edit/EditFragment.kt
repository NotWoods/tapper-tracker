package com.tigerxdaphne.tappertracker.pages.edit

import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.databinding.FragmentEditBinding
import com.tigerxdaphne.tappertracker.db.toUtcDate
import com.tigerxdaphne.tappertracker.viewBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

/**
 * Fragment used to edit properties on a TappedTag.
 */
class EditFragment : Fragment() {

    private val args by navArgs<EditFragmentArgs>()
    private val viewModel by viewModel<EditViewModel> { parametersOf(args) }
    private var binding by viewBinding<FragmentEditBinding>()
    private lateinit var reminderUnitAdapter: TimeUnitAdapter
    private lateinit var confirmOnExit: OnBackPressedCallback

    /**
     * Sets up confirm-on-exit behaviour for the fragment.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        confirmOnExit = requireActivity().onBackPressedDispatcher.addCallback(
            owner = this,
            enabled = false
        ) {
            val binding = binding
            if (binding == null) {
                // No context, if we got here somehow just exit
                findNavController().navigateUp()
                return@addCallback
            }

            val context = binding.root.context
            val customName = binding.nameField.text.toString()

            ConfirmationAlertDialog(context, customName, args.isNew) { _, which ->
                when (which) {
                    // Trigger save
                    BUTTON_POSITIVE -> saveTag()
                    // Exit without saving
                    BUTTON_NEGATIVE -> findNavController().navigateUp()
                    // Cancel does nothing
                }
            }.show()
        }
    }

    /**
     * Inflate the view and set up click/text change listeners.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditBinding.inflate(inflater, container, false)
        this.binding = binding

        reminderUnitAdapter = TimeUnitAdapter(binding.root.context)
        binding.reminderUnitField.setAdapter(reminderUnitAdapter)

        binding.nameField.setText(args.tag.name)
        binding.nameField.requestFocus()
        binding.nameField.doAfterTextChanged { text ->
            confirmOnExit.isEnabled = true
            viewModel.onNameChange(text)
        }

        binding.notesField.setText(args.tag.notes)

        onReminderDateChanged(args.tag.reminder)

        binding.reminderDurationField.doAfterTextChanged {
            confirmOnExit.isEnabled = true
            val durationLong = it.toString().toLongOrNull()
            reminderUnitAdapter.setPluralFor(durationLong, viewModel.timeUnits)
            updateUnitField()

            onReminderPeriodChanged(
                duration = durationLong,
                unitPosition = viewModel.reminderUnitPosition
            )
        }

        binding.reminderUnitField.listSelection = viewModel.reminderUnitPosition
        binding.reminderUnitField.setOnItemClickListener { _, _, position, _ ->
            confirmOnExit.isEnabled = true
            viewModel.reminderUnitPosition = position
            onReminderPeriodChanged(
                duration = binding.reminderDurationField.text?.toString()?.toLongOrNull(),
                unitPosition = position
            )
        }

        binding.on.setOnClickListener {
            showDatePicker()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.nameError.observe(viewLifecycleOwner) { binding!!.name.error = it }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit, menu)
    }

    /**
     * Handle the Up and Save menu buttons.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (confirmOnExit.isEnabled) {
                    confirmOnExit.handleOnBackPressed()
                } else {
                    findNavController().navigate(EditFragmentDirections.actionEditFragmentToListFragment())
                }
                true
            }
            R.id.action_save -> {
                saveTag()
                true
            }
            else -> false
        }
    }

    private fun onReminderPeriodChanged(duration: Long?, unitPosition: Int) {
        duration ?: return
        val timeUnit = viewModel.timeUnits.getOrNull(unitPosition) ?: return
        val date = args.tag.lastSet.plus(duration, timeUnit)

        binding?.on?.text = viewModel.formatDate(date)
    }

    private fun onReminderDateChanged(date: LocalDate) {
        val binding = binding ?: return

        viewModel.reminderDate = date
        binding.on.text = viewModel.formatDate(date)

        val days = viewModel.daysUntil(date)
        binding.reminderDurationField.setText(days.toString())
        reminderUnitAdapter.setPluralFor(days.toLong(), viewModel.timeUnits)

        val selectedUnitIndex = viewModel.timeUnits.indexOf(ChronoUnit.DAYS)
        binding.reminderUnitField.listSelection = selectedUnitIndex
        updateUnitField()
    }

    private fun updateUnitField() {
        var selectionIndex = binding!!.reminderUnitField.listSelection
        if (selectionIndex == -1) {
            selectionIndex = viewModel.timeUnits.indexOf(ChronoUnit.DAYS)
        }

        binding!!.reminderUnitField.setText(reminderUnitAdapter.getItem(selectionIndex))
    }

    private fun showDatePicker() {
        val datePicker = viewModel.buildDatePicker()

        datePicker.addOnPositiveButtonClickListener { selection ->
            confirmOnExit.isEnabled = true
            val selectedDate = Instant.ofEpochMilli(selection).toUtcDate()
            onReminderDateChanged(selectedDate)
        }

        datePicker.show(parentFragmentManager, datePicker.toString())
    }

    /**
     * Validate the text fields then save the tag.
     */
    private fun saveTag() = lifecycleScope.launch {
        val customName = binding!!.nameField.text?.toString()
        val notes = binding!!.notesField.text?.toString()

        if (viewModel.saveTag(requireContext(), customName, notes)) {
            findNavController().navigate(EditFragmentDirections.actionEditFragmentToListFragment())
        }
    }
}
