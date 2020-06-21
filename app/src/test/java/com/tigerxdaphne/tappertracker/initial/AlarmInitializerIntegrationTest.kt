package com.tigerxdaphne.tappertracker.initial

import android.content.Context
import androidx.startup.AppInitializer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tigerxdaphne.tappertracker.notify.AlarmScheduler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.get

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class AlarmInitializerIntegrationTest : KoinTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun testIntegrationWithAppInitializer() {
        val scheduler = AppInitializer.getInstance(context)
            .initializeComponent(AlarmInitializer::class.java)

        assertEquals(get<AlarmScheduler>(), scheduler)
    }
}
