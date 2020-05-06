package com.tigerxdaphne.tappertracker.db

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate
import org.threeten.bp.Period
import org.threeten.bp.ZonedDateTime

class TimeConverters {

    @TypeConverter
    fun fromTimestamp(value: String): LocalDate = LocalDate.parse(value)

    @TypeConverter
    fun dateToTimestamp(date: LocalDate): String = date.toString()
}