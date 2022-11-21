package com.shopping.jetpacks.source.data.face

import com.shopping.base.BaseDataPattern
import com.shopping.jetpacks.entities.ProductBookmarkRequest
import com.shopping.jetpacks.entities.ProductModel
import com.shopping.jetpacks.entities.ProductRequest
import kotlinx.coroutines.flow.Flow

interface ProductData : BaseDataPattern {

    suspend fun dbInsert(models: List<ProductModel>)

    suspend fun dbUpdate(model: ProductModel)

    suspend fun dbSelectById(id: String): ProductModel

    suspend fun dbSelect(): Flow<List<ProductModel>>

    suspend fun dbSelectSimilar(request: ProductRequest): Flow<List<ProductModel>>

    suspend fun dbSelectBookmarkCount(): Flow<Int>

    suspend fun dbGetBookmark(): List<ProductModel>

    suspend fun dbSelectBookmark(): Flow<List<ProductModel>>

    suspend fun dbUpdateFavorite(request: ProductBookmarkRequest)
}
