package com.peter.pretest

import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

object PretestFunction {

    fun removeSpace (str: String): ArrayList<String>{
        var newString = ""
        val stringArray = arrayListOf<String>()
        str.forEach {
            when {
                it.toString() == " " ->{
                    stringArray.add(newString)
                    newString = ""
                }
                else -> {
                    newString += it
                }
            }
        }
        stringArray.add(newString)
        return stringArray
    }

    fun findName (str: String): String{
        val array = removeSpace(str)
        Log.d("peter","the array value = $array")
        var newString = ""
        for (i in 0 until array.size){
            when {
                array[i] == "At" -> {
                }
                array[i] == "Platform" -> {
                    return  newString
                }
                array[i] == "platform" -> {
                    return  newString
                }
                else -> {
                    newString += array[i]
                }
            }
        }
        return newString
    }

    fun findApproachName (str: String): String{
        val array = removeSpace(str)
        Log.d("peter","the array value = $array")
        var newString = ""
        for (i in 0 until array.size){
            when {
                array[i] == "Approaching" -> {
                }
                array[i] == "Platform" -> {
                    return  newString
                }
                else -> {
                    newString += array[i]
                }
            }
        }
        return newString
    }

    fun findLeftName (str: String): String{
        val array = removeSpace(str)
        Log.d("peter","the array value = $array")
        var newString = ""
        for (i in 0 until array.size){
            when {
                array[i] == "Left" -> {
                }
                else -> {
                    newString += array[i]
                }
            }
        }
        return newString
    }

//    fun removeLastSpace(str: String): String{
//
//        if ( str.isNotEmpty() && str[str.length - 1].toString() == " ") {
//            str.substring(0, str.length - 1);
//        }
//        return str
//    }
}