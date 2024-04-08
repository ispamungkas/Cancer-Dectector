package com.dicoding.asclepius.datasouce.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.model.InspectionModel

@Dao
interface DatabaseService {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(inspectionModel: InspectionModel)

    @Query("SELECT * FROM INSPECTIONMODEL")
    fun queryAllHistory(): LiveData<List<InspectionModel>>
}