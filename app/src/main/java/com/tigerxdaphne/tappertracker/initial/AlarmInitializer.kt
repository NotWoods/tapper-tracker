package com.tigerxdaphne.tappertracker.initial

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.startup.Initializer
import com.tigerxdaphne.tappertracker.notify.AlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.inject
import org.koin.core.qualifier.qualifier
import java.time.Clock
import java.time.LocalDate

/**
 * Schedules an alarm as soon as the app starts up.
 */
@Suppress("Unused")
class AlarmInitializer : Initializer<AlarmScheduler>, KoinComponent {

    private val alarmScheduler: AlarmScheduler by inject()
    private val clock: Clock by inject()
    private val coroutineScope: CoroutineScope by inject(processScope)

    override fun create(context: Context): AlarmScheduler {
        coroutineScope.launch {
            alarmScheduler.scheduleUpcomingReminderAlarm(context, today = LocalDate.now(clock))
        }

        return alarmScheduler
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = listOf(
        KoinInitializer::class.java
    )
}
