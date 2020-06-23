package com.example.flightmobileapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocalHostsViewModel(application: Application) : AndroidViewModel(application) {

    var allLocalHost: List<LocalHost>? = null
    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(localHost: LocalHost, localHostDao: LocalHostDao?) = viewModelScope.launch(Dispatchers.IO) {
        updateListFromDataBase(localHostDao)
        var size: Int? = null
        if (allLocalHost != null) {
            size = allLocalHost?.size
        }
        if (size !=null && size < 5) {
            localHostDao?.insertLocalHost(localHost)
        } else {
            var str : String = allLocalHost!![0].localHost
            deleteLocalHost(str , localHostDao)
            localHostDao?.insertLocalHost(localHost)
        }
    }

    fun updateListFromDataBase(localHostDao: LocalHostDao?) = viewModelScope.launch(Dispatchers.IO) {
        allLocalHost = localHostDao?.getLocalHosts()
    }

    fun clearAllTable(localHostDao: LocalHostDao?) = viewModelScope.launch(Dispatchers.IO) {
        localHostDao?.clearAllTable()
    }

    fun getAllList(localHostDao: LocalHostDao?) : List<LocalHost>? {
        updateListFromDataBase(localHostDao)
        Thread.sleep(200)
        return this.allLocalHost
    }

    private fun deleteLocalHost(localHost: String, localHostDao: LocalHostDao?) = viewModelScope.launch(Dispatchers.IO) {
        localHostDao?.deleteByName(localHost)
    }
}
