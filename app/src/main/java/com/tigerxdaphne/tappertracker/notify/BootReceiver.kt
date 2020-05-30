package com.tigerxdaphne.tappertracker.notify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BOOT_COMPLETED
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.time.LocalDate

/**
 * Handles device restart broadcasts, which clear out alarms.
 */
class BootReceiver : BroadcastReceiver(), KoinComponent {

    private val alarmScheduler: AlarmScheduler by inject()
    private val coroutineScope: CoroutineScope by inject()

    /**
     * Device has restarted, re-schedule the next alarm
     */
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_BOOT_COMPLETED) {
            val today = LocalDate.now()
            coroutineScope.launch { alarmScheduler.scheduleUpcomingReminderAlarm(context, today) }
        }
    }
}
