package com.peter.pretest.data.source.local

import android.content.Context
import com.peter.pretest.data.Result
import com.peter.pretest.data.StopPoints
import com.peter.pretest.data.ArrivalInfo
import com.peter.pretest.data.LineSequence
import com.peter.pretest.data.source.PretestDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class PretestLocalDataSource(val context: Context) : PretestDataSource {

    override suspend fun getNearbyStation(lat: Double, lon: Double, radius: Int): Result<StopPoints> {
        TODO("Not yet implemented")
    }

    override suspend fun getArrivalInfo(id: String): Result<List<ArrivalInfo>> {
        TODO("Not yet implemented")
    }

    override suspend fun getLineSequence(stationName: String): Result<LineSequence> {
        TODO("Not yet implemented")
    }


}