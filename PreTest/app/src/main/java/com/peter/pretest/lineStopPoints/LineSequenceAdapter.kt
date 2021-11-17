package com.peter.pretest.lineStopPoints

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.peter.pretest.PretestFunction
import com.peter.pretest.databinding.ItemStopsSequenceCellBinding
import okhttp3.internal.notify

class LineSequenceAdapter(private val currentLocation: String, private val currentTrainLocation : String) :
    ListAdapter<String, LineSequenceAdapter.ViewHolder>(DiffCallback) {


    class ViewHolder(private var binding: ItemStopsSequenceCellBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stationNames: String, currentLocation: String, currentTrainLocation : String) {

            binding.nameStations = stationNames

            if (currentLocation == stationNames && currentLocation.length == stationNames.length) {

                binding.name.typeface = Typeface.DEFAULT_BOLD
                binding.name.setTextColor(Color.BLACK)
            } else {
                binding.name.typeface = Typeface.DEFAULT
                binding.name.setTextColor(Color.GRAY)
            }

            // for checking the status of between
            if (currentTrainLocation.contains("Between")){

                val str = PretestFunction.removeSpace(currentTrainLocation)[PretestFunction.removeSpace(currentTrainLocation).lastIndex]
                if (stationNames.contains(str)){
                    binding.markerTop.visibility = View.VISIBLE
                }else{
                    binding.markerTop.visibility = View.GONE
                }
            }

            // for checking the status of At Platform
            if (currentTrainLocation == "At Platform"){
                if (currentLocation == stationNames && currentLocation.length == stationNames.length){
                    binding.marker.visibility = View.VISIBLE
                }else{
                    binding.marker.visibility = View.GONE
                }
            }

            // for checking the status of At
            if (currentTrainLocation.contains("At") && currentTrainLocation != "At Platform"){
                val str = PretestFunction.findName(currentTrainLocation)
                if (stationNames.contains(str)){
                    binding.marker.visibility = View.VISIBLE
                }else{
                    binding.marker.visibility = View.GONE
                }
            }

            // for checking the status of Approaching
            if (currentTrainLocation.contains("Approaching")){
                val str = PretestFunction.findApproachName(currentTrainLocation)
                if (stationNames.contains(str)){
                    binding.markerTop.visibility = View.VISIBLE
                }else{
                    binding.markerTop.visibility = View.GONE
                }
            }

            // for checking the status of Left
            if (currentTrainLocation.contains("Left")){
                val str = PretestFunction.findLeftName(currentTrainLocation)
                if (stationNames.contains(str)){
                    binding.markerBottom.visibility = View.VISIBLE
                }else{
                    binding.markerBottom.visibility = View.GONE
                }
            }

            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemStopsSequenceCellBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), currentLocation, currentTrainLocation)
    }


    companion object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

}