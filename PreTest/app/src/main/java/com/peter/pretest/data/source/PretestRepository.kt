package com.peter.pretest.data.source

import com.peter.pretest.data.ArrivalInfo
import com.peter.pretest.data.LineSequence
import com.peter.pretest.data.Result
import com.peter.pretest.data.StopPoints

/**
 *
 * Interface to the Pretest layers.
 */

interface PretestRepository {

    suspend fun getNearbyStation(lat: Double, lon: Double, radius: Int): Result<StopPoints>

    suspend fun getArrivalInfo(id: String): Result<List<ArrivalInfo>>

    suspend fun getLineSequence(stationName: String): Result<LineSequence>

}