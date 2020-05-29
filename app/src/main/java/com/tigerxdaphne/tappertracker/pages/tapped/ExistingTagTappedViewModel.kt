package com.tigerxdaphne.tappertracker.pages.tapped

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tigerxdaphne.tappertracker.db.TappedRepository
import com.tigerxdaphne.tappertracker.db.TappedTag
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.time.LocalDate
import java.time.Period

class ExistingTagTappedViewModel(
    private val existingTag: TappedTag
) : ViewModel(), KoinComponent {

    private val repository: TappedRepository by inject()

    fun stop() = viewModelScope.launch {
        repository.updateTag(existingTag.copy(lastSet = today(), isStopped = true))
    }

    fun reset() = viewModelScope.launch {
        val reminderLength =  Period.between(existingTag.lastSet, existingTag.reminder)
        val newTag = existingTag.copy(
            lastSet = today(),
            reminder = today().plus(reminderLength),
            isStopped = false
        )
        repository.updateTag(newTag)
    }

    @VisibleForTesting
    internal fun today() = LocalDate.now()

    class Factory(private val args: ExistingTagTappedFragmentArgs) : ViewModelProvider.Factory {
        @Suppress("Unchecked_Cast")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = ExistingTagTappedViewModel(
            args.tag
        ) as T
    }
}