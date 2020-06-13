package com.example.flightmobileapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(LocalHost::class), version = 1, exportSchema = false)
public abstract class LocalHostsRoomDatabase : RoomDatabase() {

    abstract fun localHostDao(): LocalHostDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: LocalHostsRoomDatabase? = null

        fun getDatabase(context: Context): LocalHostsRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalHostsRoomDatabase::class.java,
                    "localhost_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}