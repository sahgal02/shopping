package com.shopping.jetpacks.usecase

import com.shopping.base.BaseUseCase
import com.shopping.jetpacks.entities.ProductResponse
import com.shopping.jetpacks.extensions.ResourceState
import com.shopping.jetpacks.repo.impls.ProductRepoImpl
import com.shopping.jetpacks.viewmodels.ProductVM
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ProductUseCase] is a Repo Class will do every calculation and link transaction either api based or database based
 * For more details & links check [ProductVM]
 *
 * @param repo [ProductRepoImpl] for Apis Transactions
 */
class ProductUseCase @Inject constructor(
    private val repo: ProductRepoImpl,
) : BaseUseCase<Unit, ResourceState<ProductResponse>>() {

    /**
     * Perform Logout Operation and Clear DB Items
     * Update user image : [.apiPut]
     */
    override fun run(params: Unit?): ProductUseCase {
        launch {
            execute(repo.fetchData())
        }
        return this
    }

}