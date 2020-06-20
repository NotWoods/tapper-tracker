package com.tigerxdaphne.tappertracker.initial

import android.content.Context
import androidx.startup.Initializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.KoinContextHandler
import org.koin.core.context.startKoin

/**
 * Sets up Koin when the app starts up.
 */
class KoinInitializer : Initializer<KoinContextHandler> {

    override fun create(context: Context): KoinContextHandler {
        if (koinNotStarted()) {
            startKoin {
                androidContext(context)
                modules(databaseModule, systemServiceModule, alarmModule)
            }
        }

        return KoinContextHandler
    }

    /**
     * Roboelectric tests run in parallel so Koin gets set up multiple times.
     * This check avoids multiple starts.
     */
    private fun koinNotStarted() = KoinContextHandler.getOrNull() == null

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
