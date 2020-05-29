package com.tigerxdaphne.tappertracker.pages.edit

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.annotation.PluralsRes
import com.tigerxdaphne.tappertracker.R
import java.time.temporal.ChronoUnit
import java.lang.Exception

/**
 * Adapter for [android.widget.AutoCompleteTextView] that displays chrono units.
 * It can be updated to reflect the correct plural form.
 */
class TimeUnitAdapter(
    context: Context
) : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1) {

    init {
        // We'll manage notifyDataSetChanged calls ourselves
        setNotifyOnChange(false)
    }

    override fun getFilter(): Filter = FilterNothing()

    /**
     * Update
     */
    fun setPluralFor(quantity: Long?, chronoUnits: List<ChronoUnit>) {
        val strings = chronoUnits.map {
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

    private class FilterNothing : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults? = null
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) = Unit
    }
}
