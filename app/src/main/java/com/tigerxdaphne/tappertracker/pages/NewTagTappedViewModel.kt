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

class NewTagTappedViewModel : ViewModel(), KoinComponent {
    private val repository: TappedRepository by inject()

    fun onNewTag(tag: Tag) = viewModelScope.launch {
        val tappedTag = TappedTag(
            id = tag.id,
            lastSet = LocalDate.now(),
            reminder = LocalDate.now().plusWeeks(1)
        )
        repository.addTag(tappedTag)
    }
}
