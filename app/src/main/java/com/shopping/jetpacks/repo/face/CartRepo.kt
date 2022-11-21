package com.shopping.jetpacks.repo.face

import com.shopping.jetpacks.entities.CartModel
import com.shopping.jetpacks.entities.ProductModel
import kotlinx.coroutines.flow.Flow

interface CartRepo {
    suspend fun updateOrAdd(model: CartModel)

    suspend fun select(): List<CartModel>

    suspend fun selectCount(): Flow<Int>

    suspend fun clear()

    suspend fun selectById(param: ProductModel): List<CartModel>
}