package com.shopping.utilities

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.shopping.jetpacks.repo.face.ConfigRepo
import com.shopping.prefs.TempStorage
import com.shopping.prefs.UserStorage
import com.shopping.utilities.MyApplication.Companion.instance
import com.shopping.variables.interfaces.LanguageKeys
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


/**
 * [android.app.Application] class Initialize Important SDK's like [FirebaseApp] and Maintain User Session info and User Profile
 */
@HiltAndroidApp
class MyApplication : MultiDexApplication() {

    @Inject
    lateinit var userStorage: UserStorage

    @Inject
    lateinit var tempStorage: TempStorage

    @Inject
    lateinit var repo: ConfigRepo

    /**
     * AppCompatDelegate property enable Vector Resources
     */
    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        configRepo = this.repo
    }


    companion object {

        /**
         * [MyApplication] Class [instance] to access objects and [getApplicationContext]
         */
        @get:Synchronized
        @Volatile
        lateinit var instance: MyApplication
            private set

        var languageId: Int = LanguageKeys.LANG_ENGLISH

        @get:Synchronized
        @Volatile
        lateinit var configRepo: ConfigRepo

        var cartTotal: Double = 0.toDouble()
        var itemCount: Int = 0
    }
}