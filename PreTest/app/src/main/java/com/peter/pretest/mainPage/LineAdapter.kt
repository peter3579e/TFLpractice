package com.peter.pretest.mainPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.peter.pretest.NavigationDirections
import com.peter.pretest.data.ArrivalInfo
import com.peter.pretest.data.ArrivalandStation
import com.peter.pretest.databinding.ItemLineCellsBinding
import com.peter.pretest.databinding.ItemStationsCellBinding

class LineAdapter : ListAdapter<ArrivalInfo, LineAdapter.ViewHolder>(DiffCallback) {


    class ViewHolder(private var binding: ItemLineCellsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(arrivalInfo: ArrivalInfo) {
            binding.info = arrivalInfo
            binding.executePendingBindings()


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLineCellsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            it.findNavController().navigate(
                NavigationDirections.navigateToLinePointsFragment(
                    getItem(position).lineName!!,
                    getItem(position).stationName!!,
                    getItem(position).destinationName!!,
                    getItem(position).currentLocation!!
                )
            )
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<ArrivalInfo>() {
        override fun areItemsTheSame(oldItem: ArrivalInfo, newItem: ArrivalInfo): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ArrivalInfo, newItem: ArrivalInfo): Boolean {
            return oldItem.expectedArrival == newItem.expectedArrival
        }
    }

}