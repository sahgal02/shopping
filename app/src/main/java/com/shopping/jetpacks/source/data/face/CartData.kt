package com.shopping.jetpacks.source.data.face

import com.shopping.base.BaseDataPattern
import com.shopping.jetpacks.entities.CartModel
import kotlinx.coroutines.flow.Flow

interface CartData : BaseDataPattern {

    suspend fun dbInsert(model: CartModel)

    suspend fun dbSelectCount(): Flow<Int>

    suspend fun dbSelect(): List<CartModel>

    suspend fun dbSelectById(id: String): List<CartModel>

    suspend fun dbRemove(model: CartModel)

}
