package com.shopping.jetpacks.extensions

import android.annotation.SuppressLint
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import retrofit2.Call

@SuppressLint("DefaultLocale")
fun <F, T> Call<F>.mapToEntity(
    mapper: (F) -> T
): ResourceState<T> {
    try {
        val response = this.execute()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                if (body is JSONObject && dataCheck(
                        body,
                        "status"
                    ) && !body.getBoolean("status") && dataCheck(
                        body,
                        "message"
                    )
                ) {
                    return ResourceState.Failure(
                        Exception(body.getString("message")),
                        code = response.code()
                    )
                } else {
                    @Suppress("UNCHECKED_CAST")
                    return ResourceState.Success(mapper(body as F))
                }
            }
        }
        return ResourceState.Failure(
            Exception("Connection interrupted, Please try again"),
            code = response.code()
        )
    } catch (e: Exception) {
        return if (e.message != null && e.message.equals("Canceled")) {
            ResourceState.Cancelled(e)
        } else
            ResourceState.Failure(e)
    }
}

fun dataCheck(jsonObject: JSONObject?, key: String): Boolean {
    return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key)
}

sealed class ResourceState<T> {
    data class Success<T>(val body: T, val code: Int? = 200) : ResourceState<T>()

    data class Failure<T>(
        val exception: Throwable,
        val code: Int = 500
    ) : ResourceState<T>()

    data class Cancelled<T>(
        val exception: Throwable,
    ) : ResourceState<T>()
}

/**
 * Check api result is successful
 */
fun isApiSuccessful(it: Any): Boolean {
    return it is ResourceState.Success<*>
}

/**
 * Check api result is successful
 */
fun isApiFailed(it: Any): Boolean {
    return it is ResourceState.Failure<*>
}
