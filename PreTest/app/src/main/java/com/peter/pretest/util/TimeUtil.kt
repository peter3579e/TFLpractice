package com.peter.pretest.util

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    @SuppressLint("SimpleDateFormat")
    fun stampToTime (str: String) : String{
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val formatter: DateFormat = SimpleDateFormat("HH:mm:ss")
        val date: Date = dateFormat.parse(str) //You will get date object relative to server/client timezone wherever it is parsed
        return formatter.format(date)
    }
}