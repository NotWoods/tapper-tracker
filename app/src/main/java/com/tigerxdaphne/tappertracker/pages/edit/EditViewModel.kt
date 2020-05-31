package com.tigerxdaphne.tappertracker.pages.edit

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.tigerxdaphne.tappertracker.db.TappedRepository
import com.tigerxdaphne.tappertracker.db.TappedTag
import com.tigerxdaphne.tappertracker.db.TappedTagModel
import com.tigerxdaphne.tappertracker.notify.AlarmScheduler
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

class EditViewModel(
    private val originalTag: TappedTag,
    private val isNew: Boolean
) : ViewModel(), KoinComponent {

    private val repository: TappedRepository by inject()
    private val alarmScheduler: AlarmScheduler by inject()

    /** Formats dates in a user-readable form. */
    val dateFieldFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    /** List of chrono units presented to the user as choices in the [EditFragment]. */
    val timeUnits = listOf(
        ChronoUnit.DAYS,
        ChronoUnit.WEEKS,
        ChronoUnit.MONTHS,
        ChronoUnit.YEARS
    )

    var reminderDate: LocalDate = originalTag.reminder
    val timeZone: ZoneId = ZoneOffset.UTC

    fun buildDatePicker(): MaterialDatePicker<Long> {
        val minDay = originalTag.lastSet.plusDays(1).toEpochMilli()

        val constraints = CalendarConstraints.Builder()
            .setStart(minDay)
            .setValidator(MinDateValidator(minDay))
            .build()

        return MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraints)
            .setSelection(reminderDate.toEpochMilli())
            .build()
    }

    /**
     * Returns the number of days from the tag's last set date to the given [reminderDate].
     */
    fun daysUntil(reminderDate: LocalDate) =
        Period.between(originalTag.lastSet, reminderDate).days

    fun saveTag(
        context: Context,
        customName: String,
        notes: String
    ) = viewModelScope.launch {
        val editedTag = TappedTagModel.fromInterface(originalTag).copy(
            reminder = reminderDate,
            name = customName,
            notes = notes
        )

        if (isNew) {
            repository.addTag(editedTag)
        } else {
            repository.updateTag(editedTag)
        }
        alarmScheduler.scheduleUpcomingReminderAlarm(context, LocalDate.now())
    }

    private fun LocalDate.toEpochMilli() = atStartOfDay(timeZone).toInstant().toEpochMilli()

    class Factory(private val args: EditFragmentArgs) : ViewModelProvider.Factory {
        @Suppress("Unchecked_Cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = EditViewModel(
            originalTag = args.tag,
            isNew = args.isNew
        ) as T
    }
}
