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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.databinding.FragmentEditBinding
import com.tigerxdaphne.tappertracker.viewBinding
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Fragment used to edit properties on a TappedTag.
 */
class EditFragment : Fragment() {

    private val args by navArgs<EditFragmentArgs>()
    private val viewModel by viewModels<EditViewModel> {
        EditViewModel.Factory(args)
    }
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
                    BUTTON_POSITIVE -> viewLifecycleOwner.lifecycleScope.launch { saveTag() }
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

        binding.nameField.setText(args.tag.customName)
        if (args.isNew) {
            binding.nameField.requestFocus()
        }
        binding.nameField.doAfterTextChanged { text ->
            confirmOnExit.isEnabled = true
            if (!text.isNullOrBlank()) binding.name.error = null
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
                unitPosition = binding.reminderUnitField.listSelection
            )
        }
        binding.reminderUnitField.setOnItemClickListener { _, _, position, _ ->
            confirmOnExit.isEnabled = true
            onReminderPeriodChanged(
                duration = binding.reminderDurationField.text?.toString()?.toLongOrNull(),
                unitPosition = position
            )
        }

        binding.dateField.setOnClickListener {
            showDatePicker()
        }

        return binding.root
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
                viewLifecycleOwner.lifecycleScope.launch { saveTag() }
                true
            }
            else -> false
        }
    }

    private fun onReminderPeriodChanged(duration: Long?, unitPosition: Int) {
        duration ?: return
        val timeUnit = viewModel.timeUnits.getOrNull(unitPosition) ?: return
        val date = args.tag.lastSet.plus(duration, timeUnit)

        binding?.dateField?.setText(date.format(viewModel.dateFieldFormatter))
    }

    private fun onReminderDateChanged(date: LocalDate) {
        val binding = binding ?: return

        viewModel.reminderDate = date
        binding.dateField.setText(date.format(viewModel.dateFieldFormatter))

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
            val selectedDate = Instant.ofEpochMilli(selection)
                .atZone(viewModel.timeZone)
                .toLocalDate()
            onReminderDateChanged(selectedDate)
        }

        datePicker.show(parentFragmentManager, datePicker.toString())
    }

    /**
     * Validate the text fields then save the tag.
     */
    private suspend fun saveTag() {
        val customName = binding!!.nameField.text.toString()
        if (customName.isBlank()) {
            binding!!.name.error = getString(R.string.tag_name_blank_error)
            return
        }

        viewModel.saveTag(customName, binding!!.notesField.text.toString())
        findNavController().navigate(EditFragmentDirections.actionEditFragmentToListFragment())
    }
}
