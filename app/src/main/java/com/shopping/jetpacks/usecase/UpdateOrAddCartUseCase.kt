package com.shopping.jetpacks.usecase

import com.shopping.base.BaseRoomUseCase
import com.shopping.jetpacks.entities.CartModel
import com.shopping.jetpacks.repo.face.CartRepo
import com.shopping.jetpacks.repo.impls.ProductRepoImpl
import com.shopping.jetpacks.viewmodels.ProductVM
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [UpdateOrAddCartUseCase] is a Repo Class will do every calculation and link transaction either api based or database based
 * For more details & links check [ProductVM]
 *
 * @param repo [ProductRepoImpl] for Apis Transactions
 */
class UpdateOrAddCartUseCase @Inject constructor(
    private val repo: CartRepo,
) : BaseRoomUseCase<CartModel, Unit>() {

    override fun run(param: CartModel): BaseRoomUseCase<CartModel, Unit> {
        launch {
            repo.updateOrAdd(param)
        }
        return this
    }
}