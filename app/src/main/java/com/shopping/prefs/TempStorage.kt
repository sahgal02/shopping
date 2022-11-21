package com.shopping.prefs

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Temp storage used for App related configurations
 */
@Singleton
class TempStorage @Inject constructor(
    @ApplicationContext applicationContext: Context
) {
    private val sharedPreferences: SharedPreferences =
        applicationContext.getSharedPreferences(SHARE_TEMP_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val SHARE_TEMP_NAME = "story_temp_pref"
    }
}