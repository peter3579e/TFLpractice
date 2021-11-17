package com.peter.pretest.data

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Source(
//  @Json(name = "children") var children : List<Station>? = null,
  @Json(name = "commonName") var commonName: String? = null,
  @Json(name = "id") var id: String? = null
) : Parcelable {}