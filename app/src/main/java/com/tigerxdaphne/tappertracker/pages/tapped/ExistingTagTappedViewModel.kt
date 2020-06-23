package com.tigerxdaphne.tappertracker.pages.tapped

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tigerxdaphne.tappertracker.db.TappedRepository
import com.tigerxdaphne.tappertracker.db.TappedTagModel
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalDate
import java.time.Period

class ExistingTagTappedViewModel(
    args: ExistingTagTappedFragmentArgs,
    private val clock: Clock,
    private val repository: TappedRepository
) : ViewModel() {

    private val existingTag = args.tag

    fun stop() = viewModelScope.launch {
        val updatedTag = TappedTagModel.fromInterface(existingTag).copy(
            lastSet = today(),
            isStopped = true
        )
        repository.updateTag(updatedTag)
    }

    fun reset() = viewModelScope.launch {
        val reminderLength = Period.between(existingTag.lastSet, existingTag.reminder)
        val newTag = TappedTagModel.fromInterface(existingTag).copy(
            lastSet = today(),
            reminder = today().plus(reminderLength),
            isStopped = false
        )
        repository.updateTag(newTag)
    }

    private fun today() = LocalDate.now(clock)
}
