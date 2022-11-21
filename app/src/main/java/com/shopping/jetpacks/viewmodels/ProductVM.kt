package com.shopping.jetpacks.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.shopping.base.BaseViewModel
import com.shopping.jetpacks.entities.ProductBookmarkRequest
import com.shopping.jetpacks.entities.ProductModel
import com.shopping.jetpacks.entities.ProductRequest
import com.shopping.jetpacks.entities.ProductResponse
import com.shopping.jetpacks.extensions.ResourceState
import com.shopping.jetpacks.source.network.impl.ProductSourceImpl
import com.shopping.jetpacks.usecase.BookmarkUseCase
import com.shopping.jetpacks.usecase.GetProductByIdUseCase
import com.shopping.jetpacks.usecase.GetProductUseCase
import com.shopping.jetpacks.usecase.ProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductVM @Inject constructor(
    val useCase: ProductUseCase,
    private val bookmarkUseCase: BookmarkUseCase,
//    private val getBookmarkUseCase: GetBookmarkUseCase,
    private val productUserCase: GetProductUseCase,
    private val getByIdUserCase: GetProductByIdUseCase
) : BaseViewModel(useCase) {

    val fetchLiveData = Transformations.switchMap(
        useCase.getData()
    ) {
        val liveData = MutableLiveData<ResourceState<ProductResponse>>()
        liveData.postValue(it)
        liveData
    }

    //
//    val bookmarkLiveData = Transformations.switchMap(
//        getBookmarkUseCase.getData()
//    ) {
//        val liveData = MutableLiveData<List<ProductModel>>()
//        liveData.postValue(it)
//        liveData
//    }
    val productLiveData = Transformations.switchMap(
        productUserCase.getData()
    ) {
        val liveData = MutableLiveData<List<ProductModel>>()
        liveData.postValue(it)
        liveData
    }

    val getByIdLiveData = Transformations.switchMap(
        getByIdUserCase.getData()
    ) {
        val liveData = MutableLiveData<ProductModel>()
        liveData.postValue(it)
        liveData
    }

    fun updateBookmark(request: ProductBookmarkRequest) = bookmarkUseCase.run(
        request
    )

    fun selectById(id: String) =
        getByIdUserCase.run(
            ProductRequest(
                id = id,
                type = ProductRequest.Type.SELECT_BY_ID
            )
        )


    fun getProduct(request: ProductRequest) =
        productUserCase.run(request)

    /**
     * Api call to fetch token : [ProductSourceImpl.apiFetchProducts]
     */
    fun fetchProducts() {
        useCase.run()
    }

}