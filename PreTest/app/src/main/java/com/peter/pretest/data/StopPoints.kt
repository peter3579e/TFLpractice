package com.peter.pretest.data

import com.squareup.moshi.Json

data class StopPoints(
  @Json(name = "stopPoints") var stop: List<Source>
)
