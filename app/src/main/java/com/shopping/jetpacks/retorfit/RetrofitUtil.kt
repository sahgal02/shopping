package com.shopping.jetpacks.retorfit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Retrofit Util for Retrofit Initialization and Instance signup
 *
 */
class RetrofitUtil {
    val gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    fun getTypeToken(typeToken: TypeToken<*>): Type {
        return typeToken.type
    }

    fun toJson(`object`: Any?, type: Type?): String {
        return gson.toJson(`object`, type)
    }

    fun <T> fromJson(data: String, classOfT: Class<T>): T {
        return gson.fromJson(data, classOfT)
    }

    fun <T> fromJson(data: String, type: Type?): T {
        return gson.fromJson(data, type)
    }

    companion object {
        /**
         * Get singleton implementation of Retrofit Util to access its Data Members [RetrofitInterface] and [Gson]
         * @update : Implementing datatype implementation which means Assign once
         */
        val instance: RetrofitUtil = RetrofitUtil()
    }
}