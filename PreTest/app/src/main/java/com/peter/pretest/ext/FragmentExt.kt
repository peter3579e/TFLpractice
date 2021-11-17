package com.peter.pretest.ext

import androidx.fragment.app.Fragment
import com.peter.pretest.PretestApplication
import com.peter.pretest.factory.LineStopViewModelFactory
import com.peter.pretest.factory.ViewModelFactory
/**
 *
 * Extension functions for Fragment.
 */
fun Fragment.getVmFactory(): ViewModelFactory {
    val repository = (requireContext().applicationContext as PretestApplication).preTestRepository
    return ViewModelFactory(repository)
}

fun Fragment.getVmFactory(name:String, currentName: String, toward: String): LineStopViewModelFactory{
    val repository = (requireContext().applicationContext as PretestApplication).preTestRepository
    return LineStopViewModelFactory(name, currentName, toward, repository)
}