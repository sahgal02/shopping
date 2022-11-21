package com.shopping.jetpacks.usecase

import com.shopping.base.BaseRoomUseCase
import com.shopping.jetpacks.entities.ProductModel
import com.shopping.jetpacks.entities.ProductRequest
import com.shopping.jetpacks.repo.impls.ProductRepoImpl
import com.shopping.jetpacks.viewmodels.ProductVM
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [GetProductByIdUseCase] is a Repo Class will do every calculation and link transaction either api based or database based
 * For more details & links check [ProductVM]
 *
 * @param repo [ProductRepoImpl] for Apis Transactions
 */
class GetProductByIdUseCase @Inject constructor(
    private val repo: ProductRepoImpl,
) : BaseRoomUseCase<ProductRequest, ProductModel>() {

    override fun run(param: ProductRequest): BaseRoomUseCase<ProductRequest, ProductModel> {
        launch {
            execute(
                repo.selectById(param)
            )
        }
        return this
    }
}