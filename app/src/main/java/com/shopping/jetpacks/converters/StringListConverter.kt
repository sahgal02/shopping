package com.shopping.jetpacks.converters

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken

import com.shopping.jetpacks.retorfit.RetrofitUtil

/**
 * Type converter for Array of [String]
 */
class StringListConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromStringToArray(value: String): List<String> =
            RetrofitUtil.instance.fromJson(
                value, RetrofitUtil.instance.getTypeToken(object :
                    TypeToken<List<String>>() {})
            )

        @TypeConverter
        @JvmStatic
        fun fromArrayToString(data: List<String>): String =
            RetrofitUtil.instance.toJson(
                data,
                RetrofitUtil.instance.getTypeToken(object :
                    TypeToken<List<String>>() {})
            )
    }

}