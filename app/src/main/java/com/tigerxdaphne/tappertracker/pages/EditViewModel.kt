package com.tigerxdaphne.tappertracker.pages

import androidx.annotation.PluralsRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tigerxdaphne.tappertracker.R

class EditViewModel : ViewModel() {
    val timeUnits = TimeUnit.values()

    val reminderDuration: LiveData<Int>
    val reminderUnit: LiveData<TimeUnit>
}

enum class TimeUnit(@PluralsRes val resId: Int) {
    DAY(R.plurals.time_unit_day),
    WEEK(R.plurals.time_unit_week),
    MONTH(R.plurals.time_unit_month),
    YEAR(R.plurals.time_unit_year)
}
