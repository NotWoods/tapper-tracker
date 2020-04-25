package com.tigerxdaphne.tappertracker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TappedTag::class], version = 1)
@TypeConverters(TimeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tappedTagDao(): TappedTagDao
}