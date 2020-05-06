package com.tigerxdaphne.tappertracker.notify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_BOOT_COMPLETED

class BootReceiver : BroadcastReceiver() {

    /**
     * Device has restarted, re-schedule the next alarm
     */
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_BOOT_COMPLETED) {
            // Set the alarm here.
        }

    }
}
