package com.peter.pretest.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.peter.pretest.PretestApplication

object Util {

    /**
     * Determine and monitor the connectivity status
     *
     * https://developer.android.com/training/monitoring-device-state/connectivity-monitoring
     */

    fun isInternetConnected(): Boolean {
        val cm = PretestApplication.instance
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun getString(resourceId: Int): String {
        return PretestApplication.instance.getString(resourceId)
    }

}