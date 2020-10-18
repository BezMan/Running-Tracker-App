package com.example.android.runningtrackerapp.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.android.runningtrackerapp.R
import com.example.android.runningtrackerapp.other.CustomMarkerView
import com.example.android.runningtrackerapp.other.TrackingUtility
import com.example.android.runningtrackerapp.ui.viewmodels.StatisticsViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlin.math.round

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val viewModel: StatisticsViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setupBarChart()
    }


    @SuppressLint("SetTextI18n")
    private fun subscribeToObservers() {
        viewModel.totalTimeRun.observe(viewLifecycleOwner, {
            it?.let {
                tvTotalTime.text = TrackingUtility.getFormattedTimer(it)
            }
        })
        viewModel.totalDistance.observe(viewLifecycleOwner, {
            it?.let {
                val km = it / 1000f
                val totalDistance = round(km * 10f) / 10f
                tvTotalDistance.text = "${totalDistance}km"
            }
        })
        viewModel.totalAvgSpeed.observe(viewLifecycleOwner, {
            it?.let {
                val avgSpeed = round(it * 10f) / 10f
                tvAverageSpeed.text = "${avgSpeed}km/h"
            }
        })
        viewModel.totalCaloriesBurned.observe(viewLifecycleOwner, {
            it?.let {
                tvTotalCalories.text = "${it}kcal"
            }
        })
        viewModel.runSortedByDate.observe(viewLifecycleOwner, {
            it?.let{
                val allAvgSpeeds = it.indices.map { i ->
                    BarEntry(i.toFloat(), it[i].avgSpeedKMH)
                }
                val barDataSet = BarDataSet(allAvgSpeeds, "Avg Speed Over Time").apply {
                    valueTextColor = Color.WHITE
                    color = ContextCompat.getColor(requireContext(), R.color.colorAccent)
                }
                barChart.data = BarData(barDataSet)
                barChart.invalidate()
                barChart.marker = CustomMarkerView(it, requireContext(), R.layout.marker_view)
            }
        })

    }

    private fun setupBarChart(){
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        barChart.axisLeft.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        barChart.axisRight.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        barChart.apply {
            description.text = "Avg Speed Over Time"
            legend.isEnabled = false
        }

    }

}