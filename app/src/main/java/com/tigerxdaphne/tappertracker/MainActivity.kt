package com.tigerxdaphne.tappertracker

import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        when {
            // NFC not supported on the device
            nfcAdapter == null -> {}
            // NFC is turned off
            !nfcAdapter.isEnabled -> {}
            // Good to go
            else -> {}
        }
    }
}
