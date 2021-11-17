package com.peter.pretest.mainPage

import android.Manifest
import android.content.pm.PackageManager
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

    private var arrival = ArrayList<ArrivalInfo>()

    private var size = 0

    private var stations = ArrayList<Source>()

    private var stationWithInfo = ArrayList<ArrivalandStation>()

    lateinit var mainHandler: Handler

    lateinit var updateTextTask: Runnable

    private var emptyStationList = ArrayList<ArrivalandStation>()


    override fun onStart() {
        super.onStart()
        startLocationPermissionRequest()
    }

    private fun startLocationPermissionRequest() {
    try {
        if (ContextCompat.checkSelfPermission(PretestApplication.instance.baseContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
        }else{
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

        stationWithInfo = arrayListOf()
        arrival = arrayListOf()
        stations = arrayListOf()
        viewModel.arrivalInfo.value = null

        Log.d("peter","station with info = $stationWithInfo arrival =  $arrival")

        binding = FragmentMainpageBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val adapter = StationAdapter()
        binding.recyclerView.adapter = adapter
        mainHandler = Handler(Looper.getMainLooper())
        viewModel.location.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (lon != it.longitude && lat != it.latitude){
                    lon = it.longitude
                    lat = it.latitude
                    Log.d("peter","lon = ${it.longitude} and lat = ${it.latitude}")
                    viewModel.getNearByStation(it.latitude,it.longitude,400)
                }
            }
        })


        viewModel.stationList.observe(viewLifecycleOwner, Observer { stationList ->
            stationList?.let {
                size = stationList.stop.size
                stationList.stop.forEach {
                    viewModel.getArrivalInfo(it.id!!)
                    stations.add(it)
                  stationWithInfo.add(ArrivalandStation())
                }
                emptyStationList = stationWithInfo
            }
        })

        updateTextTask = object : Runnable {
            override fun run() {
                mainHandler.postDelayed(this, 30000)
                stationWithInfo = emptyStationList
                    stations.forEach {
                        viewModel.getArrivalInfo(it.id!!)
                    }
            }
        }

        var name = ""


        viewModel.arrivalInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.d("peter","here has run 3 + $it")
                //change the time format
                for (arrivalInfo in it) {
                    name = arrivalInfo.stationName!!
                    if (arrival.size < 3)  {
//                    if (arrivalInfo.direction == "inbound" && arrival.size < 3) {
                        val time = arrivalInfo.expectedArrival?.let { it1 ->
                            TimeUtil.stampToTime(it1)
                        }
                        arrivalInfo.expectedArrival = time
                        arrival.add(arrivalInfo)
                    }
                }

              arrival.sortWith(compareBy<ArrivalInfo> {it.expectedArrival})

                stations.forEachIndexed { index, source ->
                    if (source.commonName == name){
                        stationWithInfo[index] = ArrivalandStation(source,arrival)
                    }
                }

                arrival = arrayListOf()

                Log.d("peter","$stationWithInfo")

                adapter.submitList(stationWithInfo)
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
        if (requestCode == 101) {
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

