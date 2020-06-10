package com.example.flightmobileapp

import androidx.lifecycle.LiveData

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class LocalHostsRepository(private val localHostDao: LocalHostDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allLocalHosts: LiveData<List<LocalHosts>> = localHostDao.getAlphabetizedWords()

    suspend fun insert(localHost: LocalHosts) {
        localHostDao.insert(localHost)
    }
}