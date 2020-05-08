package com.tigerxdaphne.tappertracker

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.tigerxdaphne.tappertracker.pages.AddTagFragmentArgs

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNfc()
    }

    private fun setupNfc() {
        val nfcAdapter = NfcAdapter.getDefaultAdapter(this) ?: return
        val flags = NfcAdapter.FLAG_READER_NFC_A or
                NfcAdapter.FLAG_READER_NFC_B or
                NfcAdapter.FLAG_READER_NFC_BARCODE or
                NfcAdapter.FLAG_READER_NFC_F or
                NfcAdapter.FLAG_READER_NFC_V or
                NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
        nfcAdapter.enableReaderMode(this, this::onTagDiscovered, flags, null)
    }

    private fun onTagDiscovered(tag: Tag) {
        navController.navigate(R.id.newTagTappedFragment, AddTagFragmentArgs(tag).toBundle())
    }
}
