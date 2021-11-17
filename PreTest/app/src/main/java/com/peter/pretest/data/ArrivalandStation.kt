package com.peter.pretest.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


data class ArrivalandStation(
        val source: Source? = null,
        val arrivalInfoList: List<ArrivalInfo>? = null
)