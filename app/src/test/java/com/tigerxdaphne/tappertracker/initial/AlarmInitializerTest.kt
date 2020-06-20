package com.tigerxdaphne.tappertracker.initial

import android.content.Context
import com.tigerxdaphne.tappertracker.notify.AlarmScheduler
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@ExperimentalCoroutinesApi
class AlarmInitializerTest : KoinTest {

    private val clock = Clock.fixed(Instant.EPOCH, ZoneOffset.UTC)
    private val now = LocalDate.now(clock)
    private val coroutineScope = TestCoroutineScope()
    @MockK private lateinit var context: Context
    @MockK(relaxed = true) private lateinit var scheduler: AlarmScheduler
    private lateinit var initializer: AlarmInitializer

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        startKoin {
            modules(module {
                single { clock }
                single { scheduler }
                single<CoroutineScope>(processScope) { coroutineScope }
            })
        }

        initializer = AlarmInitializer()
    }

    @After
    fun teardown() {
        stopKoin()
    }

    @Test
    fun testSchedulesAlarmOnStart() = coroutineScope.runBlockingTest {
        assertEquals(scheduler, AlarmInitializer().create(context))

        coVerify { scheduler.scheduleUpcomingReminderAlarm(context, now) }
    }

    @Test
    fun testDependenciesIncludesKoin() {
        val deps = initializer.dependencies()
        assertTrue(KoinInitializer::class.java in deps)
    }
}
