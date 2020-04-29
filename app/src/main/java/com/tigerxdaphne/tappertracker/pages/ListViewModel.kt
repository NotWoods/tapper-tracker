package com.tigerxdaphne.tappertracker.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tigerxdaphne.tappertracker.db.TappedRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class ListViewModel : ViewModel(), KoinComponent {
    private val repository: TappedRepository by inject()

    val tags = repository.getAllTags().asLiveData(viewModelScope.coroutineContext)
}
