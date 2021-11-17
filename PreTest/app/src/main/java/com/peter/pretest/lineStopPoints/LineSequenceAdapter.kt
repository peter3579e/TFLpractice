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
import com.peter.pretest.databinding.ItemStopsSequenceCellBinding

class LineSequenceAdapter(private val currentLocation: String) : ListAdapter<String,LineSequenceAdapter.ViewHolder>(DiffCallback) {


    class ViewHolder(private var binding: ItemStopsSequenceCellBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stationNames: String, currentLocation: String) {

            binding.nameStations = stationNames

            if (currentLocation == stationNames && currentLocation.length == stationNames.length){
                Log.d("peter","same has run currentlocation $currentLocation and stationName = $stationNames")
                binding.name.typeface = Typeface.DEFAULT_BOLD
                binding.marker.visibility = View.VISIBLE
                binding.name.setTextColor(Color.BLACK)
            }else{
                binding.name.typeface = Typeface.DEFAULT
                binding.marker.visibility = View.GONE
                binding.name.setTextColor(Color.GRAY)
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
            holder.bind(getItem(position),currentLocation)
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