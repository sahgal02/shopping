package com.shopping.utilities

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import org.json.JSONObject
import javax.inject.Singleton

/**
 * [Log] function for ease creating console using common key [key]
 *
 * @param key Unique identity key, Please edit auto key according to you...
 * @param message any object for console
 *
 */
fun storyLogs(message: Any?, key: String? = "jamun") {
    Log.d(key, message.toString())
}

/**
 *  Most of Utils function for ease like [setVisibility], [helperDialog].
 */
@Singleton
object Utils {

    fun keyCheck(jsonObject: JSONObject?, key: String): Boolean {
        return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key)
    }

    /**
     * [Toast] function for ease using [message] and [context] of Page
     */
    fun toast(context: Context, message: String?) {
        message?.let {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Function used for Status bar color white
     */
    fun doStatusColorWhite(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }

    /**
     * Check network connectivity and information regarding network enable
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (CheckOs.checkBuildMarshmallow()) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

}
