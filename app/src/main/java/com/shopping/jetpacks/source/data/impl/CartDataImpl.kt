package com.shopping.jetpacks.source.data.impl

import com.shopping.jetpacks.daos.CartDao
import com.shopping.jetpacks.entities.CartModel
import com.shopping.jetpacks.entities.ProductModel
import com.shopping.jetpacks.source.data.face.CartData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * [CartDataImpl] manage all Room transaction of [ProductModel] using [CoroutineScope]
 */
class CartDataImpl @Inject constructor(
    private val dao: CartDao
) : CartData {

    /**
     * Insert list of [ProductModel] into [dao]
     */
    override suspend fun dbInsert(model: CartModel) {
        dao.insert(model)
    }

    override suspend fun dbRemove(model: CartModel) {
        dao.remove(model)
    }

    override suspend fun dbSelectCount(): Flow<Int> {
        return dao.selectCount()
    }

    /**
     * Select [ProductModel] all or by id : [dao]
     */
    override suspend fun dbSelect(): List<CartModel> {
        return dao.select()
    }

    /**
     * Select [ProductModel] all or by id : [dao]
     */
    override suspend fun dbSelectById(id: String): List<CartModel> {
        return dao.selectById(id + "_")
    }

    /**
     * Clear DB
     */
    override suspend fun dbClear() {
        dao.clear()
    }

}