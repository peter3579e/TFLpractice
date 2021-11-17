package com.peter.pretest.data

import com.squareup.moshi.Json

data class ArrivalInfo(
        @Json (name = "stationName") var stationName: String? = null,
        @Json (name = "direction") var direction: String? = null,
        @Json (name = "currentLocation") var currentLocation: String? = null,
        @Json (name = "towards") var towards: String? = null,
        @Json (name = "lineName") var lineName: String? = null,
        @Json (name = "platformName") var platformName: String? = null,
        @Json (name = "destinationName") var destinationName: String? = null,
        @Json (name = "expectedArrival") var expectedArrival: String? = null
)
