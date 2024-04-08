package com.dicoding.asclepius.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class InspectionModel (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Int?,

    @ColumnInfo("imageUri")
    val imageUri: String,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("confidenceScore")
    val confidenceScore: String,

    @ColumnInfo("timesTime")
    val timesTime: String
): Parcelable