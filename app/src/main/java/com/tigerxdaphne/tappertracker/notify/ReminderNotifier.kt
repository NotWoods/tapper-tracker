package com.tigerxdaphne.tappertracker.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tigerxdaphne.tappertracker.R
import com.tigerxdaphne.tappertracker.db.TappedRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.time.LocalDate

const val NOTIFICATION_CHANNEL_ID = "tag_reminder"

class ReminderNotifier : KoinComponent {

    private val repository: TappedRepository by inject()
    private val notificationManager: NotificationManagerCompat by inject()

    /**
     * Display a notification with all that tags that have reminders set for [today].
     */
    suspend fun displayReminderNotification(context: Context, today: LocalDate) {
        val tagsDueToday = repository.getAllRemindersOnDate(today)

        if (tagsDueToday.isEmpty()) return

        if (SDK_INT >= Build.VERSION_CODES.O &&
            notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null) {
            NotificationChannel(
               NOTIFICATION_CHANNEL_ID,
               "Tag reminders",
               IMPORTANCE_DEFAULT
            ).apply {
                enableLights(true)
                setShowBadge(true)
            }.also {
                notificationManager.createNotificationChannel(it)
            }
        }

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Don't forget about your tags today!")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // TODO change icon
            .setContentText(tagsDueToday.joinToString(", ") { it.name })
            .setAutoCancel(true)
            .build()

        notificationManager.notify(today.toNotificationId(), notification)
    }

    private fun LocalDate.toNotificationId(): Int = (year * 1000) + dayOfYear
}
