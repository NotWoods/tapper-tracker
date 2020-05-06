package com.tigerxdaphne.tappertracker.notify

import android.app.AlarmManager
import android.content.Context
import com.tigerxdaphne.tappertracker.db.TappedRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId

class AlarmScheduler : KoinComponent {

    private val repository: TappedRepository by inject()
    private val alarmManager: AlarmManager by inject()

    /**
     * Sets up an alarm to fire when the next reminder is ready.
     * The alarm is triggered after the start of the day, when Android feels like it
     * (phone is awake).
     */
    suspend fun scheduleUpcomingReminderAlarm(context: Context, today: LocalDate) {
        val tag = repository.getUpcomingReminder(today) ?: return

        val alarmIntent = AlarmReceiver.createPendingIntent(context)
        val alarmTime = tag.reminder.atStartOfDay(ZoneId.systemDefault())

        alarmManager.set(
            AlarmManager.RTC,
            alarmTime.toInstant().toEpochMilli(),
            alarmIntent
        )
    }
}
