package com.tigerxdaphne.tappertracker

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.tigerxdaphne.tappertracker.koin.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TapperTrackerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        startKoin {
            androidContext(this@TapperTrackerApplication)
            modules(databaseModule)
        }
    }
}
