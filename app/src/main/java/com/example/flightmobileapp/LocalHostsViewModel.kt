package com.example.flightmobileapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocalHostsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LocalHostsRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    private val allLocalHosts: LiveData<List<LocalHosts>>

    init {
        val localHostsDao = LocalHostsRoomDatabase.getDatabase(application).localHostDao()
        repository = LocalHostsRepository(localHostsDao)
        allLocalHosts = repository.allLocalHosts
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(localHost: LocalHosts) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(localHost)
    }
}