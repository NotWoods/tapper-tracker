package com.tigerxdaphne.tappertracker.pages.list

import android.content.Context
import android.nfc.NfcAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tigerxdaphne.tappertracker.db.TappedRepository
import java.time.Clock
import java.time.LocalDate

class ListViewModel(
    repository: TappedRepository,
    val clock: Clock
) : ViewModel() {

    val tags = repository.getAllTags().asLiveData(viewModelScope.coroutineContext)

    private var today: LocalDate = LocalDate.now(clock)

    /**
     * Returns true if NFC is supported on the device.
     */
    fun deviceSupportsNfc(context: Context): Boolean {
        val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)
        return nfcAdapter != null
    }

    /**
     * Checks if the date is different since the last call to this function.
     * Used to refresh the list if the app was last open on a different day.
     */
    fun dateChanged(): Boolean {
        val newDate = LocalDate.now(clock)
        return if (newDate == today) {
            false
        } else {
            today = newDate
            true
        }
    }
}
