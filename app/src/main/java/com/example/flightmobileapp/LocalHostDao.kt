package com.example.flightmobileapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flightmobileapp.LocalHosts

@Dao
interface LocalHostDao {
    @Query("SELECT * from localHosts_table ORDER BY localHost ASC")
    fun getAlphabetizedWords(): LiveData<List<LocalHosts>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(localHost: LocalHosts)

    @Query("DELETE FROM localHosts_table")
    suspend fun deleteAll()
}