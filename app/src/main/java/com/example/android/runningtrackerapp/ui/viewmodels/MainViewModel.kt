package com.example.android.runningtrackerapp.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.runningtrackerapp.db.Run
import com.example.android.runningtrackerapp.other.SortType
import com.example.android.runningtrackerapp.repositories.MainRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
) : ViewModel() {

    private val runSortedByDate = mainRepository.getAllRunsByDate()
    private val runSortedByDistance = mainRepository.getAllRunsByDistance()
    private val runSortedByCaloriesBurned = mainRepository.getAllRunsByCaloriesBurned()
    private val runSortedByTimeInMillis = mainRepository.getAllRunsByTimeInMillis()
    private val runSortedByAvgSpeed = mainRepository.getAllRunsByAvgSpeed()

    val runs = MediatorLiveData<List<Run>>()

    var currSortType = SortType.DATE

    init {
        runs.addSource(runSortedByDate) { result ->
            if (currSortType == SortType.DATE) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runSortedByTimeInMillis) { result ->
            if (currSortType == SortType.RUN_TIME) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runSortedByDistance) { result ->
            if (currSortType == SortType.DISTANCE) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runSortedByAvgSpeed) { result ->
            if (currSortType == SortType.AVG_SPEED) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runSortedByCaloriesBurned) { result ->
            if (currSortType == SortType.CALORIES_BURNED) {
                result?.let { runs.value = it }
            }
        }
    }

    fun sortRuns(sortType: SortType) =
        when (sortType) {
            SortType.DATE -> runSortedByDate.value?.let { runs.value = it }
            SortType.RUN_TIME -> runSortedByTimeInMillis.value?.let { runs.value = it }
            SortType.DISTANCE -> runSortedByDistance.value?.let { runs.value = it }
            SortType.AVG_SPEED -> runSortedByAvgSpeed.value?.let { runs.value = it }
            SortType.CALORIES_BURNED -> runSortedByCaloriesBurned.value?.let { runs.value = it }
        }.also {
            currSortType = sortType
        }


    fun insertRun(run: Run) = viewModelScope.launch {
        mainRepository.insertRun(run)
    }
}