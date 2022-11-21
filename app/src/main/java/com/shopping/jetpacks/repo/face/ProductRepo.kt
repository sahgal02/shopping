package com.shopping.jetpacks.repo.face

import com.shopping.jetpacks.entities.ProductBookmarkRequest
import com.shopping.jetpacks.entities.ProductModel
import com.shopping.jetpacks.entities.ProductRequest
import com.shopping.jetpacks.entities.ProductResponse
import com.shopping.jetpacks.extensions.ResourceState
import com.shopping.jetpacks.source.network.impl.ProductSourceImpl
import kotlinx.coroutines.flow.Flow

interface ProductRepo {

    suspend fun select(request: ProductRequest): Flow<List<ProductModel>>

    suspend fun selectSimilar(request: ProductRequest): Flow<List<ProductModel>>

    suspend fun selectBookmark(request: ProductRequest): Flow<List<ProductModel>>

    suspend fun selectById(request: ProductRequest): ProductModel

    suspend fun updateFavorite(request: ProductBookmarkRequest)

    suspend fun updateProduct(request: ProductModel)

    suspend fun selectBookmarkCount(): Flow<Int>

    /**
     * Api call to fetch token : [ProductSourceImpl.apiFetchProducts]
     */
    suspend fun fetchData(): ResourceState<ProductResponse>

    fun closeEverything()

    suspend fun clearAll()
}