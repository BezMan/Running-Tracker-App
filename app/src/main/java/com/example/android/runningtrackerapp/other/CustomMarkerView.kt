package com.example.android.runningtrackerapp.other

import android.annotation.SuppressLint
import android.content.Context
import com.example.android.runningtrackerapp.db.Run
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.marker_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class CustomMarkerView(val runs: List<Run>, context: Context, layoutId: Int) :
    MarkerView(context, layoutId) {


    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2f, -height.toFloat())
    }


    @SuppressLint("SetTextI18n")
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)

        e?.let {
            val currRunId = e.x.toInt()
            val run = runs[currRunId]

            val calendar = Calendar.getInstance().apply {
                timeInMillis = run.timeStampRunStart
            }
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            tvDate.text = dateFormat.format(calendar.time)

            tvAvgSpeed.text = "${run.avgSpeedKMH}km/h"

            tvDistance.text = "${run.distanceMeters / 1000f}km"

            tvTime.text = TrackingUtility.getFormattedTimer(run.timeTotalRunMillis)

            tvCalories.text = "${run.caloriesBurned}kcal"
        }

    }
}