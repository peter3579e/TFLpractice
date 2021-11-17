package com.peter.pretest.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.peter.pretest.data.source.PretestRepository
import com.peter.pretest.mainPage.MainPageViewModel
/**
 *
 * Factory for ViewModels.
 */
class ViewModelFactory(
    private val pretestRepository: PretestRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainPageViewModel::class.java)) {
            return MainPageViewModel(pretestRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

}