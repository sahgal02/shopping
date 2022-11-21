package com.shopping.jetpacks.converters

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import com.shopping.jetpacks.entities.ProductModel

import com.shopping.jetpacks.retorfit.RetrofitUtil

/**
 * Type converter for Array of [ProductModel.PurchaseModel]
 */
class PurchaseModelConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromStringToArray(value: String): List<ProductModel.PurchaseModel> =
            RetrofitUtil.instance.fromJson(
                value, RetrofitUtil.instance.getTypeToken(object :
                    TypeToken<List<ProductModel.PurchaseModel>>() {})
            )

        @TypeConverter
        @JvmStatic
        fun fromArrayToString(data: List<ProductModel.PurchaseModel>): String =
            RetrofitUtil.instance.toJson(
                data,
                RetrofitUtil.instance.getTypeToken(object :
                    TypeToken<List<ProductModel.PurchaseModel>>() {})
            )
    }

}