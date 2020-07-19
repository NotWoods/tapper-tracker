package com.tigerxdaphne.tappertracker.db

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class TimeConverters {

    @TypeConverter
    fun fromTimestamp(value: String): LocalDate = LocalDate.parse(value)

    @TypeConverter
    fun dateToTimestamp(date: LocalDate): String = date.toString()
}

fun LocalDate.toUtcInstant(): Instant = atStartOfDay(ZoneOffset.UTC).toInstant()

fun Instant.toUtcDate(): LocalDate = atZone(ZoneOffset.UTC).toLocalDate()
