package com.shopping.jetpacks.modules

import com.shopping.BuildConfig
import com.shopping.jetpacks.retorfit.RetrofitCalls
import retrofit2.Retrofit

class ApiManager constructor(
    private val retrofit: Retrofit
) {
    val retrofitApis by lazy {
        retrofit.updateBaseUrl(BuildConfig.API_URL).createApi<RetrofitCalls>()
    }
}

inline fun <reified T> Retrofit.createApi(): T = this.create(T::class.java)

fun Retrofit.updateBaseUrl(baseUrl: String): Retrofit = this.newBuilder().baseUrl(baseUrl).build()