package com.shopping.jetpacks.converters

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken

import com.shopping.jetpacks.entities.ProductModel
import com.shopping.jetpacks.retorfit.RetrofitUtil

/**
 * Type converter for Array of [DataModel.PriceModel]
 */
class PriceModelConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromStringToArray(value: String): List<ProductModel.PriceModel> =
            RetrofitUtil.instance.fromJson(
                value, RetrofitUtil.instance.getTypeToken(object :
                    TypeToken<List<ProductModel.PriceModel>>() {})
            )

        @TypeConverter
        @JvmStatic
        fun fromArrayToString(data: List<ProductModel.PriceModel>): String =
            RetrofitUtil.instance.toJson(data,
                RetrofitUtil.instance.getTypeToken(object :
                    TypeToken<List<ProductModel.PriceModel>>() {})
            )
    }

}