package com.tigerxdaphne.tappertracker.pages.edit

import com.google.android.material.datepicker.CalendarConstraints
import kotlinx.android.parcel.Parcelize

/**
 * Validates that the selected date is greater than the given min date.
 * Both are represented as epoch milliseconds.
 */
@Parcelize
class MinDateValidator(private val minDay: Long) : CalendarConstraints.DateValidator {
    override fun isValid(date: Long): Boolean = minDay <= date
}
