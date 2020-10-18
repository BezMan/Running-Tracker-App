package com.example.android.runningtrackerapp.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_table")
data class Run(
    var img: Bitmap? = null,
    var timeStampRunStart: Long = 0L,
    var avgSpeedKMH: Float = 0f,
    var distanceMeters: Int = 0,
    var timeTotalRunMillis: Long = 0L,
    var caloriesBurned: Int = 0
) {
    @PrimaryKey(autoGenerate = true) var id: Long? = null



}