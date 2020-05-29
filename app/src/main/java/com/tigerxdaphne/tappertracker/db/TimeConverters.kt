package com.tigerxdaphne.tappertracker.db

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.Period
import java.time.ZonedDateTime

class TimeConverters {

    @TypeConverter
    fun fromTimestamp(value: String): LocalDate = LocalDate.parse(value)

    @TypeConverter
    fun dateToTimestamp(date: LocalDate): String = date.toString()
}
