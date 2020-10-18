package com.example.android.runningtrackerapp.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.android.runningtrackerapp.repositories.MainRepository

class StatisticsViewModel @ViewModelInject constructor(val mainRepository: MainRepository)
    : ViewModel()  {

    val totalTimeRun = mainRepository.getTotalRunTimeInMillis()
    val totalDistance = mainRepository.getTotalDistance()
    val totalCaloriesBurned = mainRepository.getTotalCaloriesBurned()
    val totalAvgSpeed = mainRepository.getTotalAvgSpeed()

    val runSortedByDate = mainRepository.getAllRunsByDate()

}