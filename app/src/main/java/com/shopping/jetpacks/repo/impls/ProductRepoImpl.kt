package com.shopping.jetpacks.repo.impls

import com.shopping.jetpacks.entities.ProductBookmarkRequest
import com.shopping.jetpacks.entities.ProductModel
import com.shopping.jetpacks.entities.ProductRequest
import com.shopping.jetpacks.entities.ProductResponse
import com.shopping.jetpacks.extensions.ResourceState
import com.shopping.jetpacks.extensions.isApiSuccessful
import com.shopping.jetpacks.repo.face.ProductRepo
import com.shopping.jetpacks.source.data.face.CartData
import com.shopping.jetpacks.source.data.face.ProductData
import com.shopping.jetpacks.source.network.face.ProductRemote
import com.shopping.jetpacks.source.network.impl.ProductSourceImpl
import com.shopping.utilities.MyApplication
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepoImpl @Inject constructor(
    private val remoteSource: ProductRemote,
    private val dataSource: ProductData,
    private val cartData: CartData
) : ProductRepo {

    override suspend fun select(request: ProductRequest): Flow<List<ProductModel>> {
        return when (request.type) {
            ProductRequest.Type.SELECT_EXCEPT -> {
                dataSource.dbSelectSimilar(request)
            }
            ProductRequest.Type.BOOKMARK -> {
                dataSource.dbSelectBookmark()
            }
            else -> {
                dataSource.dbSelect()
            }
        }
    }

    override suspend fun selectSimilar(request: ProductRequest): Flow<List<ProductModel>> {
        return dataSource.dbSelectSimilar(request)
    }

    override suspend fun selectBookmarkCount(): Flow<Int> {
        return dataSource.dbSelectBookmarkCount()
    }

    override suspend fun selectBookmark(request: ProductRequest): Flow<List<ProductModel>> {
        return dataSource.dbSelectBookmark()
    }

    override suspend fun selectById(request: ProductRequest): ProductModel {
        return dataSource.dbSelectById(request.id ?: "")
    }

    override suspend fun updateFavorite(request: ProductBookmarkRequest) {
        dataSource.dbUpdateFavorite(request)
    }

    override suspend fun updateProduct(request: ProductModel) {
        dataSource.dbUpdate(request)
    }

    /**
     * Api call to fetch token : [ProductSourceImpl.apiFetchProducts]
     */
    override suspend fun fetchData(): ResourceState<ProductResponse> {
        val response = remoteSource.apiFetchProducts()
        if (isApiSuccessful(response)) {
            val list = (response as ResourceState.Success).body
            val dbItems = cartData.dbSelect()
            val bookmarkItems = dataSource.dbGetBookmark()
            bookmarkItems.forEach {
                for (product in list.products ?: emptyList()) {
                    if (product.id == it.id) {
                        product.bookmark = true
                        break
                    }
                }
            }
            for (dbItem in dbItems) {
                for (product in list.products ?: emptyList()) {
                    if (dbItem.cartId.startsWith("${product.id}_")) {
                        for (purchaseItem in product.purchaseTypes ?: emptyList()) {
                            if ("${product.id}_${purchaseItem.unitPrice}" == dbItem.cartId) {
                                purchaseItem.selectedCount = dbItem.itemCount
                                product.selectedCount += dbItem.itemCount
                                MyApplication.cartTotal += purchaseItem.unitPrice ?: 0.toDouble()
                                MyApplication.itemCount += dbItem.itemCount
                                break
                            }
                        }
                        break
                    }
                }
            }
            if (!list.products.isNullOrEmpty())
                dataSource.dbInsert(list.products)
        }
        return response
    }

    override fun closeEverything() {
        remoteSource.cancelApiCalls()
    }

    override suspend fun clearAll() {
        dataSource.dbClear()
    }
}