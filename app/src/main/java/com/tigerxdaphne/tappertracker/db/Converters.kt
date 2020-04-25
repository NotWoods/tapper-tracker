package com.tigerxdaphne.tappertracker.db

import androidx.room.TypeConverter
import androidx.ui.unit.Duration
import java.util.Date

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long): Date = Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date): Long = date.time

    @TypeConverter
    fun fromNanoseconds(value: Long) = Duration(value)

    @TypeConverter
    fun durationToNanoseconds(duration: Duration) = duration.nanoseconds
}