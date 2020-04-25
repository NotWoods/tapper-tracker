package com.tigerxdaphne.tappertracker

import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import com.tigerxdaphne.tappertracker.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        when {
            // NFC not supported on the device
            nfcAdapter == null -> {}
            // Good to go
            nfcAdapter.isEnabled -> {}
            // NFC is turned off
            else -> {}
        }
    }
}
