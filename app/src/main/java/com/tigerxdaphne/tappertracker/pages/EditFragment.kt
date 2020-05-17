package com.tigerxdaphne.tappertracker.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.tigerxdaphne.tappertracker.databinding.FragmentEditBinding
import com.tigerxdaphne.tappertracker.viewBinding
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import org.threeten.bp.LocalDate

class EditFragment : Fragment() {

    private val args by navArgs<EditFragmentArgs>()
    private var binding by viewBinding<FragmentEditBinding>()
    private lateinit var reminderUnitAdapter: TimeUnitAdapter

    private val dateFieldFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditBinding.inflate(inflater, container, false)
        this.binding = binding

        reminderUnitAdapter = TimeUnitAdapter(requireContext())
        binding.reminderUnitField.setAdapter(reminderUnitAdapter)

        binding.nameField.setText(args.tag.customName)

        val date = args.tag.reminder.format(dateFieldFormatter)
        binding.dateField.setText(date)
        onReminderDateChanged(date)

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
        binding.dateField.doAfterTextChanged {
            onReminderDateChanged(it.toString())
        }

        return binding.root
    }

    private fun onReminderPeriodChanged(duration: Long?, unitPosition: Int) {
        duration ?: return
        val timeUnit = timeUnits.getOrNull(unitPosition) ?: return
        val date = args.tag.lastSet.plus(duration, timeUnit)

        binding?.dateField?.setText(date.format(dateFieldFormatter))
    }

    private fun onReminderDateChanged(dateText: String) {
        val date = LocalDate.parse(dateText, dateFieldFormatter)

        // TODO update duration and unit
    }
}
