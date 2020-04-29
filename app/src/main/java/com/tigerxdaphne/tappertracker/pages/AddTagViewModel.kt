package com.tigerxdaphne.tappertracker.pages

import android.nfc.Tag
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tigerxdaphne.tappertracker.db.TappedRepository
import com.tigerxdaphne.tappertracker.db.TappedTag
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.threeten.bp.LocalDate
import org.threeten.bp.Period

class AddTagViewModel : ViewModel(), KoinComponent {
    private val repository: TappedRepository by inject()

    fun onNewTag(tag: Tag) = viewModelScope.launch {
        val tappedTag = TappedTag(
            id = tag.id,
            lastTapped = LocalDate.now(),
            reminderDuration = Period.ofWeeks(1)
        )
        repository.addTag(tappedTag)
    }
}
