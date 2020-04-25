package com.tigerxdaphne.tappertracker.dagger

import android.content.Context
import androidx.room.Room
import com.tigerxdaphne.tappertracker.db.AppDatabase
import com.tigerxdaphne.tappertracker.db.TappedTagDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(@ApplicationContext private val context: Context) {

    @Singleton
    @Provides
    fun provideDatabase(): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "tapper_tracker_database"
        ).build()

    @Singleton
    @Provides
    fun provideTappedTagDao(db: AppDatabase): TappedTagDao = db.tappedTagDao()
}