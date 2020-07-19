package com.tigerxdaphne.tappertracker.initial

import android.app.AlarmManager
import android.content.res.Resources
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
import com.tigerxdaphne.tappertracker.pages.edit.EditFragmentArgs
import com.tigerxdaphne.tappertracker.pages.edit.EditViewModel
import com.tigerxdaphne.tappertracker.pages.list.ListViewModel
import com.tigerxdaphne.tappertracker.pages.tapped.ExistingTagTappedFragmentArgs
import com.tigerxdaphne.tappertracker.pages.tapped.ExistingTagTappedViewModel
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import java.time.Clock

val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "tapper_tracker_database"
        ).build()
    }
    single<TappedTagDao> { get<AppDatabase>().tappedTagDao() }
    single { TappedRepository(get()) }
}

val systemServiceModule = module {
    single<Resources> { androidContext().resources }
    single<AlarmManager> { androidContext().getSystemService()!! }
    single { NotificationManagerCompat.from(androidContext()) }
}

val alarmModule = module {
    single<Clock> { Clock.systemUTC() }
    single { AlarmScheduler(get(), get()) }
    single { ReminderNotifier(get(), get()) }
    single<CoroutineScope>(processScope) { ProcessLifecycleOwner.get().lifecycleScope }
}

val viewModelModule = module {
    viewModel { (args: EditFragmentArgs) -> EditViewModel(args, get(), get(), get(), get()) }
    viewModel { ListViewModel(get(), get()) }
    viewModel { (args: ExistingTagTappedFragmentArgs) -> ExistingTagTappedViewModel(args, get(), get()) }
}
