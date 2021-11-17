package com.peter.pretest.lineStopPoints

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peter.pretest.data.*
import com.peter.pretest.data.source.PretestRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LineStopViewModel(
    private val pretestRepository: PretestRepository,
    name: String,
    toward: String,
    currentName: String,
    currentLocation: String
) : ViewModel() {

    val lineName = name

    val currentStationName = currentName

    val destination = toward

    val currenLocation = currentLocation

    var idMap = hashMapOf<String, String>()

    private val _lineSequence = MutableLiveData<LineSequence>()
    val lineSequence: MutableLiveData<LineSequence>
        get() = _lineSequence

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    val map: HashMap<String, String> = hashMapOf()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getArrivalInfo(lineName)
    }


    fun getArrivalInfo(lineName: String) {
        coroutineScope.launch {
            val result = pretestRepository.getLineSequence(lineName)

            when (result) {
                is Result.Success -> {
                    _lineSequence.value = result.data
                }
                else -> null
            }
        }
    }

    fun putIntoMap(list: List<Lines>): HashMap<String, String> {
        for (line in list) {
            if (!map.containsKey(line.id) && !map.containsValue(line.name)) {
                map.put(line.id!!, line.name!!)
            }
        }
        return map
    }

    fun findLine(routList: List<Route>): ArrayList<String> {
        var lineMap = hashMapOf<Int, String>()
        var array = arrayListOf<String>()

        routList.forEach { route ->
            for (i in route.naptanIds!!.indices) {
                lineMap.put(i, idMap[route.naptanIds!![i]]!!)
                array.add(idMap[route.naptanIds!![i]]!!)
            }


            if (lineMap.containsValue(currentStationName) && lineMap.containsValue(destination)) {
                return array
            } else {
                lineMap = hashMapOf()
                array = arrayListOf()
            }
        }

        return array
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}