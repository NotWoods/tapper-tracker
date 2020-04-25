package com.tigerxdaphne.tappertracker

import android.app.Application
import com.tigerxdaphne.tappertracker.dagger.DaggerTapperTrackerComponent
import com.tigerxdaphne.tappertracker.dagger.DatabaseModule
import com.tigerxdaphne.tappertracker.dagger.TapperTrackerComponent

class TapperTrackerApplication : Application() {
    private lateinit var component: TapperTrackerComponent

    override fun onCreate() {
        super.onCreate()
        component = DaggerTapperTrackerComponent
            .builder()
            .databaseModule(DatabaseModule(this))
            .build()
    }
}
