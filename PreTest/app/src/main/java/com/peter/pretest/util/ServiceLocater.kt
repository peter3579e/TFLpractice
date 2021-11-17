package com.peter.pretest.util

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.peter.pretest.data.source.DefaultPreTestRepository
import com.peter.pretest.data.source.PretestDataSource
import com.peter.pretest.data.source.PretestRepository
import com.peter.pretest.data.source.local.PretestLocalDataSource
import com.peter.pretest.data.source.remote.PretestRemoteDataSource
/**
 * A Service Locator for the [PretestRepository].
 */
object ServiceLocater {

    @Volatile
    var preTestRepository: PretestRepository? = null
        @VisibleForTesting set

    fun provideTasksRepository(context: Context): PretestRepository {
        synchronized(this) {
            return preTestRepository
                ?: preTestRepository
                ?: createPretestRepository(context)
        }
    }

    private fun createPretestRepository(context: Context): PretestRepository {
        return DefaultPreTestRepository(PretestRemoteDataSource, createLocalDataSource(context))
    }

    private fun createLocalDataSource(context: Context): PretestDataSource {
        return PretestLocalDataSource(context)
    }

}