package com.tigerxdaphne.tappertracker.pages.list

import android.content.Context
import android.nfc.NfcAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tigerxdaphne.tappertracker.db.TappedRepository
import com.tigerxdaphne.tappertracker.db.TappedTag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.time.Clock
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModel : ViewModel(), KoinComponent {

    private val repository: TappedRepository by inject()

    val clock: Clock by inject()

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
            true
        } else {
            today = newDate
            false
        }
    }
}
