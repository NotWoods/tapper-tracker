package com.tigerxdaphne.tappertracker.pages.edit

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.db.TappedRepository
import com.tigerxdaphne.tappertracker.db.TappedTagModel
import com.tigerxdaphne.tappertracker.db.toUtcInstant
import com.tigerxdaphne.tappertracker.notify.AlarmScheduler
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

class EditViewModel(
    args: EditFragmentArgs,
    private val resources: Resources,
    private val clock: Clock,
    private val repository: TappedRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private val dateFieldFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    private val originalTag = args.tag
    private val isNew = args.isNew
    private val _nameError = MutableLiveData<String?>(null)

    /** List of chrono units presented to the user as choices in the [EditFragment]. */
    val timeUnits = listOf(
        ChronoUnit.DAYS,
        ChronoUnit.WEEKS,
        ChronoUnit.MONTHS,
        ChronoUnit.YEARS
    )

    var nameError: LiveData<String?> = _nameError
    var reminderDate: LocalDate = originalTag.reminder
    var reminderUnitPosition = 0

    /**
     * Build a [MaterialDatePicker].
     * The min day is set to be the day after today, or the day after the last set date of the tag
     * (whichever is later).
     */
    fun buildDatePicker(): MaterialDatePicker<Long> {
        val minDay = max(originalTag.lastSet, LocalDate.now(clock)).plusDays(1).toEpochMilli()

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

    fun onNameChange(name: CharSequence?) {
        if (!name.isNullOrBlank()) {
            _nameError.postValue(null)
        }
    }

    /**
     * Formats dates in a user-readable form.
     */
    fun formatDate(date: LocalDate): String {
        return resources.getString(R.string.on_date, date.format(dateFieldFormatter))
    }

    suspend fun saveTag(
        context: Context,
        customName: String?,
        notes: String?
    ): Boolean {
        if (customName.isNullOrBlank()) {
            _nameError.postValue(context.getString(R.string.tag_name_blank_error))
            return false
        }

        viewModelScope.launch {
            val editedTag = TappedTagModel.fromInterface(originalTag).copy(
                reminder = reminderDate,
                name = customName,
                notes = notes.orEmpty()
            )

            if (isNew) {
                repository.addTag(editedTag)
            } else {
                repository.updateTag(editedTag)
            }
            alarmScheduler.scheduleUpcomingReminderAlarm(context, LocalDate.now(clock))
        }.join()

        return true
    }

    private fun LocalDate.toEpochMilli() = toUtcInstant().toEpochMilli()
    private fun max(a: LocalDate, b: LocalDate) = if (a >= b) a else b
}
