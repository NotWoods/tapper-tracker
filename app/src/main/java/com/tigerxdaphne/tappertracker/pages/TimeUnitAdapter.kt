package com.tigerxdaphne.tappertracker.pages

import android.content.Context
import android.widget.ArrayAdapter
import androidx.annotation.PluralsRes
import com.tigerxdaphne.tappertracker.R
import org.threeten.bp.temporal.ChronoUnit
import java.lang.Exception

class TimeUnitAdapter(
    context: Context
) : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1) {

    init {
        setNotifyOnChange(false)
    }

    fun setPluralFor(quantity: Long?) {
        val strings = timeUnits.map {
            context.resources.getQuantityString(getResourceFor(it), quantity?.toInt() ?: 0)
        }

        clear()
        addAll(strings)
        notifyDataSetChanged()
    }

    @PluralsRes
    private fun getResourceFor(timeUnit: ChronoUnit): Int = when (timeUnit) {
        ChronoUnit.DAYS -> R.plurals.time_unit_day
        ChronoUnit.WEEKS -> R.plurals.time_unit_week
        ChronoUnit.MONTHS -> R.plurals.time_unit_month
        ChronoUnit.YEARS -> R.plurals.time_unit_year
        else -> throw Exception("$timeUnit not supported")
    }
}

val timeUnits = listOf(
    ChronoUnit.DAYS,
    ChronoUnit.WEEKS,
    ChronoUnit.MONTHS,
    ChronoUnit.YEARS
)
