package com.peter.pretest.data.source

import com.peter.pretest.data.ArrivalInfo
import com.peter.pretest.data.LineSequence
import com.peter.pretest.data.Result
import com.peter.pretest.data.StopPoints

/**
 *
 * Concrete implementation to load Pretest sources.
 */

class DefaultPreTestRepository(
    private val pretestRemoteDataSource: PretestDataSource,
    private val pretestLocalDataSource: PretestDataSource
) : PretestRepository {

    override suspend fun getNearbyStation(lat: Double, lon: Double, radius: Int): Result<StopPoints> {
        return pretestRemoteDataSource.getNearbyStation(lat, lon, radius )
    }

    override suspend fun getArrivalInfo(id: String): Result<List<ArrivalInfo>> {
        return pretestRemoteDataSource.getArrivalInfo(id)
    }

    override suspend fun getLineSequence(stationName: String): Result<LineSequence> {
        return pretestRemoteDataSource.getLineSequence(stationName)
    }

}