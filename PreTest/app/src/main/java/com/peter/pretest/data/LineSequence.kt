package com.peter.pretest.data

import com.squareup.moshi.Json

data class LineSequence(
        @Json(name = "stopPointSequences") var stopPointSequence: List<Points>? = null,
        @Json(name = "orderedLineRoutes") var orderedLineRoutes: List<Route>? = null
)

data class Points(
        @Json(name = "stopPoint") var lines: List<Lines>? = null
)

data class Lines(
        @Json(name = "name") var name: String? = null,
        @Json(name = "id") var id: String? = null
)

data class Route(
        @Json(name = "name") var name: String? = null,
        @Json(name = "naptanIds") var naptanIds: List<String>? = null
)
