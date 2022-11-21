package com.shopping.jetpacks.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.shopping.base.BaseViewModel
import com.shopping.jetpacks.entities.CartModel
import com.shopping.jetpacks.entities.ProductModel
import com.shopping.jetpacks.usecase.CartByIdUseCase
import com.shopping.jetpacks.usecase.UpdateOrAddCartUseCase
import com.shopping.jetpacks.usecase.UpdateProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartVM @Inject constructor(
    private val updateOrAddCartUseCase: UpdateOrAddCartUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val cartByIdUseCase: CartByIdUseCase
) : BaseViewModel() {

    val itemLiveData = Transformations.switchMap(
        cartByIdUseCase.getData()
    ) {
        val liveData = MutableLiveData<List<CartModel>>()
        liveData.postValue(it)
        liveData
    }

    fun fetchById(model: ProductModel) = cartByIdUseCase.run(model)

    fun updateProduct(model: ProductModel) = updateProductUseCase.run(model)

    /**
     * Add or update number of items selected for Cart
     */
    fun updateOrAdd(model: CartModel) = updateOrAddCartUseCase.run(model)

}