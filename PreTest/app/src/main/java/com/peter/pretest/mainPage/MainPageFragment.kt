package com.peter.pretest.mainPage

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.peter.pretest.PretestApplication
import com.peter.pretest.data.ArrivalInfo
import com.peter.pretest.data.ArrivalandStation
import com.peter.pretest.data.Source
import com.peter.pretest.data.StopPoints
import com.peter.pretest.databinding.FragmentMainpageBinding
import com.peter.pretest.ext.getVmFactory
import com.peter.pretest.util.TimeUtil


class MainPageFragment : Fragment() {

    private lateinit var binding: FragmentMainpageBinding

    private val viewModel by viewModels<MainPageViewModel> { getVmFactory() }

    private var lon: Double = 0.0

    private var lat: Double = 0.0

    private val REQUEST_CODE = 101

    lateinit var mainHandler: Handler

    lateinit var updateTextTask: Runnable


    override fun onStart() {
        super.onStart()
        startLocationPermissionRequest()
    }

    private fun startLocationPermissionRequest() {
        try {
            if (ContextCompat.checkSelfPermission(PretestApplication.instance.baseContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            } else {
                viewModel.getLocation(PretestApplication.instance.baseContext)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        viewModel.stationWithInfo = arrayListOf()
        viewModel.stations = arrayListOf()
        viewModel.arrivalInfo.value = null


        binding = FragmentMainpageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val adapter = StationAdapter()
        binding.recyclerView.adapter = adapter
        mainHandler = Handler(Looper.getMainLooper())

        //Get the current location, return to default London point if out side of London
        viewModel.location.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (lon != it.longitude && lat != it.latitude) {
                    lon = it.longitude
                    lat = it.latitude



                    if (viewModel.isMarkerOutsideCircle(51.52804,-0.12908,lat,lon,30000.0)) {

                        viewModel.getNearByStation(51.52804,-0.12908,400)
                    }else{
                        viewModel.getNearByStation(it.latitude, it.longitude, 400)
                    }

                }
            }
        })


        // Get arrival information for the stop
        viewModel.stationList.observe(viewLifecycleOwner, Observer { stationList ->
            stationList?.let {
                viewModel.fetchArrivalInfo(it)
            }
        })

        // For running request for every 30 seconds
        object : Runnable {
            override fun run() {
                mainHandler.postDelayed(this, 30000)
                viewModel.stationWithInfo = viewModel.emptyStationList
                viewModel.stations.forEach {
                    viewModel.getArrivalInfo(it.id!!)
                }
            }
        }.also { updateTextTask = it }

        // change the time format
        viewModel.arrivalInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(viewModel.changeTimeFormat(it))
                adapter.notifyDataSetChanged()
            }
        })

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        mainHandler.removeCallbacks(updateTextTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler.post(updateTextTask)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>,
            grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i("peter", "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                    viewModel.getLocation(requireContext())
                }
            }
        }
    }


}

