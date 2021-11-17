package com.peter.pretest.lineStopPoints

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.peter.pretest.databinding.FragmentLinestoppointBinding
import com.peter.pretest.ext.getVmFactory

class LineStopPointsFragment : Fragment() {

    private lateinit var binding: FragmentLinestoppointBinding


    private val viewModel by viewModels<LineStopViewModel> {
        getVmFactory(
            LineStopPointsFragmentArgs.fromBundle(requireArguments()).lineName,
            LineStopPointsFragmentArgs.fromBundle(requireArguments()).currentName,
            LineStopPointsFragmentArgs.fromBundle(requireArguments()).toward,
            LineStopPointsFragmentArgs.fromBundle(requireArguments()).currentLocation
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLinestoppointBinding.inflate(inflater, container, false)

        binding.title.text = viewModel.lineName

        val adapter = LineSequenceAdapter(viewModel.currentStationName, viewModel.currenLocation)

        binding.recyclerView.adapter = adapter

        binding.returnButton.setOnClickListener {
            findNavController().popBackStack()
        }

        Log.d("peter", "toward = ${viewModel.destination}")
        Log.d("peter", "currentStation = ${viewModel.currentStationName}")
        Log.d("peter", "currentTrainLocation = ${viewModel.currenLocation}")

        viewModel.lineSequence.observe(viewLifecycleOwner, Observer {
            it?.let {
                it.stopPointSequence!!.forEach { point ->
                    viewModel.idMap = viewModel.putIntoMap(point.lines!!)
                }
                Log.d("peter", "the found array = ${viewModel.findLine(it.orderedLineRoutes!!)}")
                adapter.submitList(viewModel.findLine(it.orderedLineRoutes!!))
            }
        })

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().navigateUp()
        return true
    }
}