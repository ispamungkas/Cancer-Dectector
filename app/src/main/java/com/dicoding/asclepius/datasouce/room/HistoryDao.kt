package com.dicoding.asclepius.datasouce.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.asclepius.model.InspectionModel

@Database(entities = [InspectionModel::class], version = 1, exportSchema = false)
abstract class HistoryDao : RoomDatabase() {
    abstract fun service(): DatabaseService

    companion object {
        private var instance: HistoryDao? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance = Room.databaseBuilder(
                context.applicationContext,
                HistoryDao::class.java,
                "History"
            ).build()

            instance
        }
    }
}