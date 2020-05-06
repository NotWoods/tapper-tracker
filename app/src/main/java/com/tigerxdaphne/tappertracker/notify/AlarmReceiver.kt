package com.tigerxdaphne.tappertracker.notify

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_CANCEL_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.threeten.bp.LocalDate

/**
 * Handles broadcasts fired by the AlarmManager.
 */
class AlarmReceiver : BroadcastReceiver(), KoinComponent {

    private val coroutineScope: CoroutineScope by inject()

    /**
     * Alarm went off, show a notification and schedule the next alarm
     */
    override fun onReceive(context: Context, intent: Intent) {
        val today = LocalDate.now()
        coroutineScope.launch { ReminderNotifier().displayReminderNotification(context, today) }
        coroutineScope.launch { AlarmScheduler().scheduleUpcomingReminderAlarm(context, today) }
    }

    companion object {
        fun createPendingIntent(context: Context): PendingIntent {
            val intent = Intent(context, AlarmReceiver::class.java)
            val flags = FLAG_CANCEL_CURRENT
            return PendingIntent.getBroadcast(context, 0, intent, flags)
        }
    }
}
