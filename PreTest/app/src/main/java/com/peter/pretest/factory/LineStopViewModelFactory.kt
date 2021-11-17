package com.peter.pretest.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.peter.pretest.data.source.PretestRepository
import com.peter.pretest.lineStopPoints.LineStopViewModel
import com.peter.pretest.mainPage.MainPageViewModel

class LineStopViewModelFactory(
    private val name: String,
    private val currentStationName: String,
    private val toward: String,
    private val currentLocation: String,
    private val pretestRepository: PretestRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(LineStopViewModel::class.java)) {
            return LineStopViewModel(pretestRepository, name, toward, currentStationName, currentLocation) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

}