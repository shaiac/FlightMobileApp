package com.example.flightmobileapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface  LocalHostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocalHost(localHost: LocalHost)

    @Query("SELECT * FROM localHosts_table")
    fun getLocalHosts(): List<LocalHost>

    @Query("SELECT * FROM localHosts_table WHERE localHost == :localHost")
    fun getLocalHostByName(localHost: String): List<LocalHost>?

    @Query("DELETE FROM localHosts_table")
    fun clearAllTable()

    @Query ("DELETE FROM localHosts_table WHERE localHost == :localHost")
    fun deleteByName(localHost: String)

}