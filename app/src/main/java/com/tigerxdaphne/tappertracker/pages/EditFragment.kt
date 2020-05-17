package com.tigerxdaphne.tappertracker.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.databinding.FragmentEditBinding
import com.tigerxdaphne.tappertracker.db.TappedRepository
import com.tigerxdaphne.tappertracker.viewBinding
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.Period
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import org.threeten.bp.temporal.ChronoField
import org.threeten.bp.temporal.ChronoUnit

class EditFragment : Fragment() {

    private val dateFieldFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    private val args by navArgs<EditFragmentArgs>()
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

        reminderUnitAdapter = TimeUnitAdapter(requireContext())
        binding.reminderUnitField.setAdapter(reminderUnitAdapter)

        binding.nameField.setText(args.tag.customName)

        onReminderDateChanged(args.tag.reminder)

        binding.reminderDurationField.doAfterTextChanged {
            val durationLong = it.toString().toLongOrNull()
            reminderUnitAdapter.setPluralFor(durationLong)
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
        val timeUnit = timeUnits.getOrNull(unitPosition) ?: return
        val date = args.tag.lastSet.plus(duration, timeUnit)

        binding?.dateField?.setText(date.format(dateFieldFormatter))
    }

    private fun onReminderDateChanged(date: LocalDate) {
        val binding = binding ?: return
        val period = Period.between(args.tag.lastSet, date)

        binding.dateField.setText(date.format(dateFieldFormatter))
        binding.dateField.tag = date
        binding.reminderDurationField.setText(period.days)
        binding.reminderUnitField.listSelection = timeUnits.indexOf(ChronoUnit.DAYS)
    }

    private fun showDatePicker() {
        val minDay = args.tag.lastSet.plusDays(1).getLong(ChronoField.MILLI_OF_SECOND)

        val constraints = CalendarConstraints.Builder()
            .setStart(minDay)
            .setValidator(MinDateValidator(minDay))
            .build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraints)
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = LocalDate.from(Instant.ofEpochMilli(selection))
            onReminderDateChanged(selectedDate)
        }

        datePicker.show(parentFragmentManager, datePicker.toString())
    }

    private suspend fun saveTag() {
        val repository = get<TappedRepository>()

        val editedTag = args.tag.copy(
            reminder = binding!!.dateField.tag as LocalDate,
            customName = binding!!.nameField.text.toString()
        )

        if (editedTag.customName.isBlank()) {
            Snackbar
                .make(requireView(), R.string.tag_name_blank_error, Snackbar.LENGTH_SHORT)
                .show()
            return
        }

        if (args.isNew) {
            repository.addTag(editedTag)
        } else {
            repository.updateTag(editedTag)
        }

        findNavController().navigate(EditFragmentDirections.actionEditFragmentToListFragment())
    }

    @Parcelize
    class MinDateValidator(private val minDay: Long) : CalendarConstraints.DateValidator {
        override fun isValid(date: Long): Boolean = minDay <= date
    }
}
