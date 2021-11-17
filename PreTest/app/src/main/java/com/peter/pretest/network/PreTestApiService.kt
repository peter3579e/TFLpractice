package com.peter.pretest.network

import com.peter.pretest.BuildConfig
import com.peter.pretest.PretestApplication
import com.peter.pretest.R
import com.peter.pretest.data.StopPoints
import com.peter.pretest.data.ArrivalInfo
import com.peter.pretest.data.LineSequence

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://api.tfl.gov.uk/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val appKey = PretestApplication.instance.getString(R.string.app_key)

private val client = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = when (BuildConfig.LOGGER_VISIABLE) {
            true -> HttpLoggingInterceptor.Level.BODY
            false -> HttpLoggingInterceptor.Level.NONE
        }
    })
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

/**
 * A public interface that exposes the [getNews] methods
 */
interface PreTestApiService {

    /**
     * Returns a Coroutine [Deferred] [Articles] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "top-headlines" endpoint will be requested with the GET HTTP method
     */

    @GET("StopPoint")
    suspend fun getNearByStation(
        @Query("lat") lat: Double? = null,
        @Query("lon") lon: Double? = null,
        @Query("radius") radius:Int? = null,
        @Query("stopTypes") stopTypes:String? = "NaptanMetroStation",
        @Query("useStopPointHierarchy") useStopPointHierarchy:Boolean? = true,
        @Query("app_key") app_Key: String? = appKey
    ): StopPoints


    @GET("StopPoint/{id}/Arrivals")
    suspend fun getArrivalInfo(
            @Path ("id") id: String,
            @Query("app_key") app_Key: String? = appKey
    ): List<ArrivalInfo>

    @GET("Line/{id}/Route/Sequence/inbound")
    suspend fun getLineSequence(
            @Path ("id") stationName: String,
            @Query("app_key") app_Key: String? = appKey
    ): LineSequence


}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object PreTestApi {
    val retrofitService: PreTestApiService by lazy { retrofit.create(PreTestApiService::class.java) }
}