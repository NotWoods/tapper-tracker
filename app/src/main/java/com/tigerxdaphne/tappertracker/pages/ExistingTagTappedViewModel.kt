package com.tigerxdaphne.tappertracker.pages

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tigerxdaphne.tappertracker.Event
import com.tigerxdaphne.tappertracker.db.TappedRepository
import com.tigerxdaphne.tappertracker.db.TappedTag
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.threeten.bp.LocalDate
import org.threeten.bp.Period

class ExistingTagTappedViewModel(
    private val existingTag: TappedTag
) : ViewModel(), KoinComponent {

    private val repository: TappedRepository by inject()
    private val _dismiss = MutableLiveData<DismissEvent>()

    val dismiss: LiveData<DismissEvent> get() = _dismiss

    fun stop() = viewModelScope.launch {
        repository.updateTag(existingTag.copy(lastSet = today(), isStopped = true))
        _dismiss.postValue(DismissEvent())
    }

    fun reset() = viewModelScope.launch {
        val reminderLength =  Period.between(existingTag.lastSet, existingTag.reminder)
        val newTag = existingTag.copy(
            lastSet = today(),
            reminder = today().plus(reminderLength),
            isStopped = false
        )
        repository.updateTag(newTag)
        _dismiss.postValue(DismissEvent())
    }

    @VisibleForTesting
    internal fun today() = LocalDate.now()

    class DismissEvent : Event<Unit>(Unit)
}
