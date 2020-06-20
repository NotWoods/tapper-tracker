package com.tigerxdaphne.tappertracker

import android.app.Application
import com.tigerxdaphne.tappertracker.koin.alarmModule
import com.tigerxdaphne.tappertracker.koin.databaseModule
import com.tigerxdaphne.tappertracker.koin.systemServiceModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.KoinContextHandler
import org.koin.core.context.startKoin

class TapperTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (koinNotStarted()) {
            startKoin {
                androidContext(this@TapperTrackerApplication)
                modules(databaseModule, systemServiceModule, alarmModule)
            }
        }
    }

    /**
     * Roboelectric tests run in parallel so Koin gets set up multiple times.
     * This check avoids multiple starts.
     */
    private fun koinNotStarted() = KoinContextHandler.getOrNull() == null
}
