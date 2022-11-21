package com.shopping.jetpacks.repo.impls

import com.shopping.jetpacks.entities.CartModel
import com.shopping.jetpacks.entities.ProductModel
import com.shopping.jetpacks.repo.face.CartRepo
import com.shopping.jetpacks.source.data.face.CartData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartRepoImpl @Inject constructor(
    private val dataSource: CartData
) : CartRepo {

    override suspend fun updateOrAdd(model: CartModel) {
        if (model.itemCount == 0)
            dataSource.dbRemove(model)
        else
            dataSource.dbInsert(model)
    }

    override suspend fun selectById(param: ProductModel): List<CartModel> {
        return dataSource.dbSelectById(param.id ?: "")
    }

    override suspend fun select(): List<CartModel> {
        return dataSource.dbSelect()
    }

    override suspend fun selectCount(): Flow<Int> {
        return dataSource.dbSelectCount()
    }

    override suspend fun clear() {
        dataSource.dbClear()
    }
}