package com.peter.pretest.mainPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.peter.pretest.data.ArrivalandStation
import com.peter.pretest.databinding.ItemStationsCellBinding

class StationAdapter() : ListAdapter<ArrivalandStation,StationAdapter.ViewHolder>(DiffCallback) {


    class ViewHolder(private var binding: ItemStationsCellBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var count = 0
        fun bind(arrivalInfo: ArrivalandStation) {
            binding.arrival = arrivalInfo
            binding.stationName = arrivalInfo.source?.commonName
            val adapter = LineAdapter()
            binding.hiddenLineCycle.adapter = adapter
            adapter.submitList(arrivalInfo.arrivalInfoList)

            itemView.setOnClickListener {
                if (count % 2 == 0) {
                    binding.hiddenBox.visibility = View.VISIBLE
                }else {
                    binding.hiddenBox.visibility = View.GONE
                }
                count++
            }

            binding.executePendingBindings()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                ItemStationsCellBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
                )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(getItem(position))
    }


    companion object DiffCallback : DiffUtil.ItemCallback<ArrivalandStation>() {
        override fun areItemsTheSame(oldItem: ArrivalandStation, newItem: ArrivalandStation): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ArrivalandStation, newItem: ArrivalandStation): Boolean {
            return oldItem.source!!.id == newItem.source!!.id
        }
    }

}