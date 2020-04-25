package com.tigerxdaphne.tappertracker.db

import androidx.room.TypeConverter
import org.threeten.bp.Period
import org.threeten.bp.ZonedDateTime

class TimeConverters {

    @TypeConverter
    fun fromTimestamp(value: String): ZonedDateTime = ZonedDateTime.parse(value)

    @TypeConverter
    fun dateToTimestamp(date: ZonedDateTime): String = date.toString()

    @TypeConverter
    fun fromPeriodString(value: String) = Period.parse(value)

    @TypeConverter
    fun periodToString(period: Period) = period.toString()
}