package com.tigerxdaphne.tappertracker.initial

import android.app.AlarmManager
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.tigerxdaphne.tappertracker.db.AppDatabase
import com.tigerxdaphne.tappertracker.db.TappedRepository
import com.tigerxdaphne.tappertracker.db.TappedTagDao
import com.tigerxdaphne.tappertracker.notify.AlarmScheduler
import com.tigerxdaphne.tappertracker.notify.ReminderNotifier
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "tapper_tracker_database"
        ).build()
    }
    single<TappedTagDao> { get<AppDatabase>().tappedTagDao() }
    single<TappedRepository> { TappedRepository(get()) }
}

val systemServiceModule = module {
    single<AlarmManager> { androidContext().getSystemService()!! }
    single<NotificationManagerCompat> { NotificationManagerCompat.from(androidContext()) }
}

val alarmModule = module {
    single<Clock> { Clock.systemUTC() }
    single<AlarmScheduler> { AlarmScheduler(get(), get()) }
    single<ReminderNotifier> { ReminderNotifier(get(), get()) }
    single<CoroutineScope>(processScope) { ProcessLifecycleOwner.get().lifecycleScope }
}

val processScope = qualifier<ProcessLifecycleOwner>()
