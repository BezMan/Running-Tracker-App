package com.example.android.runningtrackerapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.runningtrackerapp.R
import com.example.android.runningtrackerapp.db.Run
import com.example.android.runningtrackerapp.other.TrackingUtility
import kotlinx.android.synthetic.main.item_run.view.*
import java.text.SimpleDateFormat
import java.util.*

class RunAdapter : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    inner class RunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    val diffCallback = object : DiffUtil.ItemCallback<Run>() {
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)

    fun submitListDiff(list: List<Run>) = differ.submitList(list)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        return RunViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_run, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(run.img).into(ivRunImage)

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

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}