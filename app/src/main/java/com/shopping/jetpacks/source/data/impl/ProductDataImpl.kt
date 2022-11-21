package com.shopping.jetpacks.source.data.impl

import com.shopping.jetpacks.daos.ProductDao
import com.shopping.jetpacks.entities.ProductBookmarkRequest
import com.shopping.jetpacks.entities.ProductModel
import com.shopping.jetpacks.entities.ProductRequest
import com.shopping.jetpacks.source.data.face.ProductData
import com.shopping.utilities.storyLogs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * [ProductDataImpl] manage all Room transaction of [ProductModel] using [CoroutineScope]
 */
class ProductDataImpl @Inject constructor(
    private val dao: ProductDao
) : ProductData {

    /**
     * Insert list of [ProductModel] into [dao]
     */
    override suspend fun dbInsert(models: List<ProductModel>) {
        dao.insertAll(models)
    }

    override suspend fun dbUpdate(model: ProductModel) {
        dao.update(model)
    }

    /**
     * Select [ProductModel] all or by id : [dao]
     */
    override suspend fun dbSelect(): Flow<List<ProductModel>> {
        return dao.select()
    }

    /**
     * Select [ProductModel] all or by id : [dao]
     */
    override suspend fun dbSelectSimilar(request: ProductRequest): Flow<List<ProductModel>> {
        return dao.selectSimilar(request.id ?: "", request.size)
    }

    override suspend fun dbGetBookmark(): List<ProductModel> {
        return dao.selectBookmarkedItems()
    }

    override suspend fun dbSelectBookmark(): Flow<List<ProductModel>> {
        return dao.selectBookmarked()
    }

    override suspend fun dbSelectBookmarkCount(): Flow<Int> {
        return dao.selectBookmarkCount()
    }

    /**
     * Select [ProductModel] all or by id : [dao]
     */
    override suspend fun dbSelectById(id: String): ProductModel {
        return dao.selectById(id)
    }

    override suspend fun dbUpdateFavorite(request: ProductBookmarkRequest) {
        storyLogs(request.toString())
        dao.updateFavorite(request.id ?: "", request.isInWishlist ?: false)
    }

    /**
     * Clear DB
     */
    override suspend fun dbClear() {
        dao.clear()
    }

}