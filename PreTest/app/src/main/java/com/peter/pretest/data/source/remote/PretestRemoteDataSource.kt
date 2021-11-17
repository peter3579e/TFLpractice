package com.peter.pretest.data.source.remote

import com.peter.pretest.R
import com.peter.pretest.data.Result
import com.peter.pretest.data.StopPoints
import com.peter.pretest.data.ArrivalInfo
import com.peter.pretest.data.LineSequence
import com.peter.pretest.data.source.PretestDataSource
import com.peter.pretest.network.PreTestApi
import com.peter.pretest.util.Logger
import com.peter.pretest.util.Util.getString
import com.peter.pretest.util.Util.isInternetConnected

/**
 *
 * Implementation of the Pretest source that from network.
 */

object PretestRemoteDataSource : PretestDataSource {

    override suspend fun getNearbyStation(
        lat: Double,
        lon: Double,
        radius: Int,
    ): Result<StopPoints> {

        if (!isInternetConnected()) {
            return com.peter.pretest.data.Result.Fail(getString(R.string.internet_not_connected))
        }

        return try {

            val result = PreTestApi.retrofitService.getNearByStation(lat,lon,radius)

            com.peter.pretest.data.Result.Success(result)
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            com.peter.pretest.data.Result.Error(e)
        }
    }

    override suspend fun getArrivalInfo(id: String): Result<List<ArrivalInfo>> {
        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }

        return try {

            val result = PreTestApi.retrofitService.getArrivalInfo(id)

            Result.Success(result)
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }

    override suspend fun getLineSequence(stationName: String): Result<LineSequence> {
        if (!isInternetConnected()) {
            return Result.Fail(getString(R.string.internet_not_connected))
        }
        return try {
            val result = PreTestApi.retrofitService.getLineSequence(stationName)

            Result.Success(result)
        } catch (e: Exception) {
            Logger.w("[${this::class.simpleName}] exception=${e.message}")
            Result.Error(e)
        }
    }


}