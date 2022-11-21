package com.shopping.prefs

import android.content.Context
import android.content.SharedPreferences
import com.shopping.utilities.MyApplication
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * User storage used for User Session configurations
 */
@Singleton
class UserStorage @Inject constructor(
    @ApplicationContext applicationContext: Context
) {
    private val sharedPreferences: SharedPreferences =
        applicationContext.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE)

    /**
     * In case of logout we will remove all User Storage Information like Settings
     */
    fun logout() {
        sharedPreferences.edit().clear().apply()
    }


    companion object {
        private const val SHARE_NAME = "story_pref"
    }
}