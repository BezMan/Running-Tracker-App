package com.example.android.runningtrackerapp.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RunDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("SELECT * FROM running_table ORDER BY timeStampRunStart DESC")
    fun getAllRunsByDate(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY timeTotalRunMillis DESC")
    fun getAllRunsByTimeMillis(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY caloriesBurned DESC")
    fun getAllRunsByCaloriesBurned(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY avgSpeedKMH DESC")
    fun getAllRunsBySpeed(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY distanceMeters DESC")
    fun getAllRunsByDistance(): LiveData<List<Run>>

    @Query("SELECT SUM(timeTotalRunMillis) FROM running_table" )
    fun getTotalRunTimeInMillis(): LiveData<Long>

    @Query("SELECT SUM(caloriesBurned) FROM running_table" )
    fun getTotalCaloriesBurned(): LiveData<Int>

    @Query("SELECT SUM(distanceMeters) FROM running_table" )
    fun getTotalDistance(): LiveData<Int>

    @Query("SELECT AVG(avgSpeedKMH) FROM running_table" )
    fun getTotalAvgSpeed(): LiveData<Float>

}