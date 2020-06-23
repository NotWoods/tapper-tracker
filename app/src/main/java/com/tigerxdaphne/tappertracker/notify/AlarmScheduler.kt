package com.tigerxdaphne.tappertracker.notify

import android.app.AlarmManager
import android.content.Context
import com.tigerxdaphne.tappertracker.db.TappedRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

class AlarmScheduler(
    private val repository: TappedRepository,
    private val alarmManager: AlarmManager,
    private val timeZone: () -> ZoneId = { ZoneId.systemDefault() }
) {

    private val alarmTime = LocalTime.of(8, 0)

    /**
     * Sets up an alarm to fire when the next reminder is ready.
     * The alarm is triggered after the start of the day, when Android feels like it
     * (phone is awake).
     * @return If the alarm was set or not.
     */
    suspend fun scheduleUpcomingReminderAlarm(context: Context, after: LocalDate): Boolean {
        val tag = repository.getUpcomingReminder(after) ?: return false

        val alarmIntent = AlarmReceiver.createPendingIntent(context)
        val alarmDateTime = tag.reminder.atTime(alarmTime).atZone(timeZone())

        alarmManager.cancel(alarmIntent)
        alarmManager.set(
            AlarmManager.RTC,
            alarmDateTime.toInstant().toEpochMilli(),
            alarmIntent
        )

        return true
    }
}
