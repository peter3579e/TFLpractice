package com.peter.pretest.mainPage

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peter.pretest.PretestApplication
import com.peter.pretest.data.ArrivalInfo
import com.peter.pretest.data.Result
import com.peter.pretest.data.StopPoints
import com.peter.pretest.data.source.PretestRepository
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

    fun getArrivalInfo(id: String) {
        coroutineScope.launch {
            val result = pretestRepository.getArrivalInfo(id)

            when (result) {
                is Result.Success -> {
                    Log.d("peter","here has run viewModel")
                   _arrivalInfo.value =  result.data
                }
                else -> null
            }
        }
    }


    fun getLocation(context:Context) {

        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationListener = LocationListener { location ->
            _location.value = location
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f,locationListener)
        } catch (ex:SecurityException) {
            Toast.makeText(PretestApplication.instance.baseContext, "error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}