package com.tigerxdaphne.tappertracker.notify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BOOT_COMPLETED
import androidx.lifecycle.ProcessLifecycleOwner
import com.tigerxdaphne.tappertracker.initial.processScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.qualifier
import java.time.Clock
import java.time.LocalDate

/**
 * Handles device restart broadcasts, which clear out alarms.
 */
class BootReceiver : BroadcastReceiver(), KoinComponent {

    private val clock: Clock by inject()
    private val alarmScheduler: AlarmScheduler by inject()
    private val coroutineScope: CoroutineScope by inject(processScope)

    /**
     * Device has restarted, re-schedule the next alarm
     */
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_BOOT_COMPLETED) {
            val today = LocalDate.now(clock)
            coroutineScope.launch { alarmScheduler.scheduleUpcomingReminderAlarm(context, today) }
        }
    }
}
