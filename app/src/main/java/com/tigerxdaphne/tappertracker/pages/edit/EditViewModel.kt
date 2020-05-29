package com.tigerxdaphne.tappertracker.pages.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.tigerxdaphne.tappertracker.db.TappedRepository
import com.tigerxdaphne.tappertracker.db.TappedTag
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
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
    val timeZone: ZoneId = ZoneId.ofOffset("UTC", ZoneOffset.UTC)

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

    suspend fun saveTag(customName: String) = withContext(IO) {
        val editedTag = originalTag.copy(
            reminder = reminderDate,
            customName = customName
        )

        if (isNew) {
            repository.addTag(editedTag)
        } else {
            repository.updateTag(editedTag)
        }
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
