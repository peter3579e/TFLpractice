package com.peter.pretest.data.source

import com.peter.pretest.data.ArrivalInfo
import com.peter.pretest.data.LineSequence
import com.peter.pretest.data.Result
import com.peter.pretest.data.StopPoints
import retrofit2.http.Path
import retrofit2.http.Query

/**
 *
 * Main entry point for accessing Pretest sources.
 */

interface PretestDataSource {

    suspend fun getNearbyStation(lat: Double, lon: Double, radius: Int ): Result<StopPoints>

    suspend fun getArrivalInfo(id:String): Result<List<ArrivalInfo>>

    suspend fun getLineSequence(stationName: String): Result<LineSequence>



}