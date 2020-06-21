package com.tigerxdaphne.tappertracker.pages.edit

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.db.TappedRepository
import com.tigerxdaphne.tappertracker.db.TappedTag
import com.tigerxdaphne.tappertracker.db.TappedTagModel
import com.tigerxdaphne.tappertracker.notify.AlarmScheduler
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.time.Clock
import java.time.LocalDate
import java.time.Period
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

class EditViewModel(
    private val originalTag: TappedTag,
    private val isNew: Boolean
) : ViewModel(), KoinComponent {

    private val clock: Clock by inject()
    private val repository: TappedRepository by inject()
    private val alarmScheduler: AlarmScheduler by inject()
    private val _nameError = MutableLiveData<String?>(null)

    /** Formats dates in a user-readable form. */
    val dateFieldFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    /** List of chrono units presented to the user as choices in the [EditFragment]. */
    val timeUnits = listOf(
        ChronoUnit.DAYS,
        ChronoUnit.WEEKS,
        ChronoUnit.MONTHS,
        ChronoUnit.YEARS
    )

    var nameError: LiveData<String?> = _nameError
    var reminderDate: LocalDate = originalTag.reminder

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

    fun onNameChange(name: CharSequence?) {
        if (!name.isNullOrBlank()) {
            _nameError.postValue(null)
        }
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

    private fun LocalDate.toEpochMilli() = atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

    class Factory(
        private val args: EditFragmentArgs
    ) : ViewModelProvider.Factory {
        @Suppress("Unchecked_Cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = EditViewModel(
            originalTag = args.tag,
            isNew = args.isNew
        ) as T
    }
}
