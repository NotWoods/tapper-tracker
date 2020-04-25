package com.tigerxdaphne.tappertracker

import android.nfc.NfcManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tigerxdaphne.tappertracker.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val nfcManager: NfcManager? by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nfcAdapter = nfcManager?.defaultAdapter

        when {
            // NFC not supported on the device
            nfcAdapter == null -> {
            }
            // Good to go
            nfcAdapter.isEnabled -> {
            }
            // NFC is turned off
            else -> {
            }
        }
    }
}
