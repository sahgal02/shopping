package com.shopping.jetpacks.usecase

import com.shopping.base.BaseUseCase
import com.shopping.jetpacks.entities.ProductBookmarkRequest
import com.shopping.jetpacks.repo.face.ProductRepo
import com.shopping.jetpacks.repo.impls.ProductRepoImpl
import com.shopping.jetpacks.viewmodels.ProductVM
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [BookmarkUseCase] is a Repo Class will do every calculation and link transaction either api based or database based
 * For more details & links check [ProductVM]
 *
 * @param repo [ProductRepoImpl] for Apis Transactions
 */
class BookmarkUseCase @Inject constructor(
    private val repo: ProductRepo,
) : BaseUseCase<ProductBookmarkRequest, Unit>() {

    /**
     * Perform Logout Operation and Clear DB Items
     * Update user image : [.apiPut]
     */
    override fun run(params: ProductBookmarkRequest?): BookmarkUseCase {
        launch {
            repo.updateFavorite(params?:ProductBookmarkRequest())
        }
        return this
    }
}