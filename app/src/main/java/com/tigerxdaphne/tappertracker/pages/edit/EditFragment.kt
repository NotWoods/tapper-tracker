package com.tigerxdaphne.tappertracker.pages.edit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.tigerxdaphne.tappertracker.MainActivity
import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.databinding.FragmentEditBinding
import com.tigerxdaphne.tappertracker.viewBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.ChronoUnit

class EditFragment : Fragment() {

    private val args by navArgs<EditFragmentArgs>()
    private val viewModel by viewModels<EditViewModel> {
        EditViewModel.Factory(args)
    }
    private var binding by viewBinding<FragmentEditBinding>()
    private lateinit var reminderUnitAdapter: TimeUnitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditBinding.inflate(inflater, container, false)
        this.binding = binding

        reminderUnitAdapter = TimeUnitAdapter(binding.root.context)
        binding.reminderUnitField.setAdapter(reminderUnitAdapter)

        binding.nameField.setText(args.tag.customName)

        val days = viewModel.daysUntil(args.tag.reminder).toLong()
        reminderUnitAdapter.setPluralFor(days, viewModel.timeUnits)
        onReminderDateChanged(args.tag.reminder)

        binding.reminderDurationField.doAfterTextChanged {
            val durationLong = it.toString().toLongOrNull()
            reminderUnitAdapter.setPluralFor(durationLong, viewModel.timeUnits)
            onReminderPeriodChanged(
                duration = durationLong,
                unitPosition = binding.reminderUnitField.listSelection
            )
        }
        binding.reminderUnitField.setOnItemClickListener { _, _, position, _ ->
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).toolbar.apply {
            navigationIcon = getDrawable(context, R.drawable.ic_close)
            navigationContentDescription = getString(R.string.close)
        }
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity).toolbar.apply {
            navigationIcon = null
            navigationContentDescription = null
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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
        binding.reminderDurationField.setText(viewModel.daysUntil(date).toString())
        binding.reminderUnitField.listSelection = viewModel.timeUnits.indexOf(ChronoUnit.DAYS)
    }

    private fun showDatePicker() {
        val datePicker = viewModel.buildDatePicker()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = LocalDate.from(Instant.ofEpochMilli(selection))
            onReminderDateChanged(selectedDate)
        }

        datePicker.show(parentFragmentManager, datePicker.toString())
    }

    private suspend fun saveTag() {
        val customName = binding!!.nameField.text.toString()
        if (customName.isBlank()) {
            Snackbar
                .make(requireView(), R.string.tag_name_blank_error, Snackbar.LENGTH_SHORT)
                .show()
            return
        }

        viewModel.saveTag(customName)
        findNavController().navigate(EditFragmentDirections.actionEditFragmentToListFragment())
    }
}
