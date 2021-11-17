package com.peter.pretest.mainPage

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peter.pretest.PretestApplication
import com.peter.pretest.data.*
import com.peter.pretest.data.source.PretestRepository
import com.peter.pretest.util.TimeUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainPageViewModel(private val pretestRepository: PretestRepository) : ViewModel() {

    private val _stationList = MutableLiveData<StopPoints>()

    // The external LiveData interface to the property is immutable, so only this class can modify
    val stationList: MutableLiveData<StopPoints>
        get() = _stationList

    lateinit var locationManager: LocationManager

    private val _location = MutableLiveData<Location>()
    val location: MutableLiveData<Location>
        get() = _location




    private val _arrivalInfo = MutableLiveData<List<ArrivalInfo>>()
    val arrivalInfo: MutableLiveData<List<ArrivalInfo>>
        get() = _arrivalInfo

    var emptyStationList = ArrayList<ArrivalandStation>()


    var stations = ArrayList<Source>()


    var stationWithInfo = ArrayList<ArrivalandStation>()


    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    fun getNearByStation(lat: Double, lon: Double, radius: Int) {
        coroutineScope.launch {
            val result = pretestRepository.getNearbyStation(lat, lon, radius)

            when (result) {
                is Result.Success -> {
                    _stationList.value = result.data
                    Log.d("peter", "the received value = ${result.data}")
                }
                else -> null
            }
        }
    }

    fun isMarkerOutsideCircle(
        centerLat:Double,
        centerLng:Double,
        currentLat: Double,
        currentLng: Double,
        radius: Double
    ): Boolean {
        val distances = FloatArray(1)
        Location.distanceBetween(
            centerLat,
            centerLng,
            currentLat,
            currentLng, distances
        )
        return radius < distances[0]
    }

    fun getArrivalInfo(id: String) {
        coroutineScope.launch {
            val result = pretestRepository.getArrivalInfo(id)

            when (result) {
                is Result.Success -> {
                    _arrivalInfo.value = result.data
                    Log.d("peter", "here has run viewModel = ${result.data}")
                }
                else -> null
            }
        }
    }

    fun changeTimeFormat(list: List<ArrivalInfo>): ArrayList<ArrivalandStation> {
        var arrival = ArrayList<ArrivalInfo>()
        var name = ""
        for (it in list) {
            name = it.stationName!!
            if (arrival.size < 3) {
                val time = it.expectedArrival?.let { times ->
                    TimeUtil.stampToTime(times)
                }
                it.expectedArrival = time
                arrival.add(it)
            }
        }

        arrival.sortWith(compareBy<ArrivalInfo> { it.expectedArrival })

        stations.forEachIndexed { index, source ->
            if (source.commonName == name) {
                stationWithInfo[index] = ArrivalandStation(source, arrival)
            }
        }

        return stationWithInfo
    }


    fun fetchArrivalInfo(list: StopPoints) {
        list.stop.forEach {
            getArrivalInfo(it.id!!)
            stations.add(it)
            stationWithInfo.add(ArrivalandStation())
        }
        emptyStationList = stationWithInfo
    }


    fun getLocation(context: Context) {

        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationListener = LocationListener { location ->
            _location.value = location
        }
        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                0f,
                locationListener
            )
        } catch (ex: SecurityException) {
            Toast.makeText(PretestApplication.instance.baseContext, "error", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}