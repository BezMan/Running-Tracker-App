package com.example.android.runningtrackerapp.repositories

import com.example.android.runningtrackerapp.db.Run
import com.example.android.runningtrackerapp.db.RunDao
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val runDao: RunDao
) {

    suspend fun insertRun(run: Run) = runDao.insertRun(run)
    suspend fun deleteRun(run: Run) = runDao.deleteRun(run)


    fun getAllRunsByDate() = runDao.getAllRunsByDate()
    fun getAllRunsByDistance() = runDao.getAllRunsByDistance()
    fun getAllRunsByTimeInMillis() = runDao.getAllRunsByTimeMillis()
    fun getAllRunsByAvgSpeed() = runDao.getAllRunsBySpeed()
    fun getAllRunsByCaloriesBurned() = runDao.getAllRunsByCaloriesBurned()


    fun getTotalAvgSpeed() = runDao.getTotalAvgSpeed()
    fun getTotalDistance() = runDao.getTotalDistance()
    fun getTotalCaloriesBurned() = runDao.getTotalCaloriesBurned()
    fun getTotalRunTimeInMillis() = runDao.getTotalRunTimeInMillis()

}