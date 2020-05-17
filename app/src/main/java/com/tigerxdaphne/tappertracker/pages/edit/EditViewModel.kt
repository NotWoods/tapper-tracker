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
import org.threeten.bp.LocalDate
import org.threeten.bp.Period
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import org.threeten.bp.temporal.ChronoField
import org.threeten.bp.temporal.ChronoUnit

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

    fun buildDatePicker(): MaterialDatePicker<Long> {
        val minDay = originalTag.lastSet
            .plusDays(1)
            .atStartOfDay(ZoneId.ofOffset("UTC", ZoneOffset.UTC))
            .toInstant()
            .toEpochMilli()

        val constraints = CalendarConstraints.Builder()
            .setStart(minDay)
            .setValidator(MinDateValidator(minDay))
            .build()

        return MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraints)
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

    class Factory(private val args: EditFragmentArgs) : ViewModelProvider.Factory {
        @Suppress("Unchecked_Cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = EditViewModel(
            originalTag = args.tag,
            isNew = args.isNew
        ) as T
    }
}
