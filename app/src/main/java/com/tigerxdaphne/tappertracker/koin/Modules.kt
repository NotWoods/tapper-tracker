package com.tigerxdaphne.tappertracker.koin

import android.app.AlarmManager
import android.app.NotificationManager
import android.nfc.NfcManager
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.room.Room
import com.tigerxdaphne.tappertracker.db.AppDatabase
import com.tigerxdaphne.tappertracker.db.TappedRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "tapper_tracker_database"
        ).build()
    }
    single { get<AppDatabase>().tappedTagDao() }
    single { TappedRepository(get()) }
}

val systemServiceModule = module {
    single { androidContext().getSystemService<AlarmManager>()!! }
    single { NotificationManagerCompat.from(androidContext()) }
}
