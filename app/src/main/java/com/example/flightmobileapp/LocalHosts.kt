package com.example.flightmobileapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "localHosts_table")
data class LocalHosts(@PrimaryKey @ColumnInfo(name = "localHost") val localHost: String)