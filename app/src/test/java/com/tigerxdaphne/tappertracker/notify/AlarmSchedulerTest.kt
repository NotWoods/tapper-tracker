package com.tigerxdaphne.tappertracker.notify

import android.app.AlarmManager
import android.app.AlarmManager.RTC
import android.app.PendingIntent
import android.content.Context
import com.tigerxdaphne.tappertracker.db.TagReminder
import com.tigerxdaphne.tappertracker.db.TappedRepository
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmSchedulerTest {

    @MockK lateinit var context: Context
    @MockK lateinit var intent: PendingIntent
    @MockK lateinit var repository: TappedRepository
    @MockK(relaxUnitFun = true) lateinit var alarmManager: AlarmManager
    lateinit var scheduler: AlarmScheduler
    var timeZone: ZoneId = ZoneOffset.UTC

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockkObject(AlarmReceiver.Companion)
        every { AlarmReceiver.createPendingIntent(context) } returns intent

        timeZone = ZoneOffset.UTC
        scheduler = AlarmScheduler(repository, alarmManager) { timeZone }
    }

    @Test
    fun noopIfNoUpcomingReminder() = runBlockingTest {
        coEvery { repository.getUpcomingReminder(any()) } returns null
        assertFalse(scheduler.scheduleUpcomingReminderAlarm(context, mockk()))
        verify { AlarmReceiver wasNot Called }
        verify { alarmManager wasNot Called }
    }

    @Test
    fun setAlarmFor8am() = runBlockingTest {
        val date = LocalDate.of(2020, 1, 1)
        val tag = TagReminder(date)
        coEvery { repository.getUpcomingReminder(any()) } returns tag

        assertTrue(scheduler.scheduleUpcomingReminderAlarm(context, mockk()))

        val expectedDateTime = LocalDateTime.of(2020, 1, 1, 8, 0)
            .atZone(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
        verify { alarmManager.set(RTC, expectedDateTime, intent) }
    }

    @Test
    fun setAlarmFor8amInGivenTimeZone() = runBlockingTest {
        timeZone = ZoneId.of("America/Vancouver")

        val date = LocalDate.of(2020, 1, 1)
        val tag = TagReminder(date)
        coEvery { repository.getUpcomingReminder(any()) } returns tag

        assertTrue(scheduler.scheduleUpcomingReminderAlarm(context, mockk()))

        val expectedDateTime = LocalDateTime.of(2020, 1, 1, 8, 0)
            .atZone(timeZone)
            .toInstant()
            .toEpochMilli()
        verify { alarmManager.set(RTC, expectedDateTime, intent) }
    }
}
