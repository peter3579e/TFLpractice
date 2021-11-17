package com.peter.pretest

import android.app.Application
import com.peter.pretest.data.source.PretestRepository
import com.peter.pretest.util.ServiceLocater
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlin.properties.Delegates

/**
 *
 * An application that lazily provides a repository. Note that this Service Locator pattern is
 * used to simplify the sample. Consider a Dependency Injection framework.
 */

class PretestApplication : Application() {

    // Depends on the flavor,
    val preTestRepository: PretestRepository
        get() = ServiceLocater.provideTasksRepository(this)


    companion object {
        var instance: PretestApplication by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}