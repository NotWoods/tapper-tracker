package com.tigerxdaphne.tappertracker

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.tigerxdaphne.tappertracker.db.TappedRepository
import com.tigerxdaphne.tappertracker.pages.tapped.NewTagTappedAlertDialog
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainActivity : AppCompatActivity(R.layout.activity_main), KoinComponent {

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }
    private val repository: TappedRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(findViewById(R.id.toolbar))
        setupActionBarWithNavController(navController)

        setupNfc()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
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
        lifecycleScope.launch {
            val tappedTag = repository.getTag(tag.id)
            if (tappedTag != null) {
                navController.navigate(NavGraphDirections.actionGlobalExistingTagTappedFragment(tappedTag))
            } else {
                NewTagTappedAlertDialog(
                    this@MainActivity,
                    navController,
                    tag
                ).show()
            }
        }
    }
}
