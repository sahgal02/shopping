package com.shopping.jetpacks.converters

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken

import com.shopping.jetpacks.entities.ProductModel
import com.shopping.jetpacks.retorfit.RetrofitUtil

/**
 * Type converter for Array of [DataModel.MessageModel]
 */
class MessageModelConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromStringToArray(value: String): ProductModel.MessageModel =
            RetrofitUtil.instance.fromJson(
                value, ProductModel.MessageModel::class.java
            )

        @TypeConverter
        @JvmStatic
        fun fromArrayToString(data: ProductModel.MessageModel): String =
            RetrofitUtil.instance.toJson(data,
                RetrofitUtil.instance.getTypeToken(object :
                    TypeToken<ProductModel.MessageModel>() {})
            )
    }

}