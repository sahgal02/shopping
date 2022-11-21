package com.shopping.jetpacks.usecase

import com.shopping.base.BaseRoomUseCase
import com.shopping.jetpacks.repo.face.CartRepo
import com.shopping.jetpacks.repo.face.ProductRepo
import com.shopping.jetpacks.repo.impls.ProductRepoImpl
import com.shopping.jetpacks.viewmodels.ProductVM
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [BookmarkCountUseCase] is a Repo Class will do every calculation and link transaction either api based or database based
 * For more details & links check [ProductVM]
 *
 * @param repo [ProductRepoImpl] for Apis Transactions
 */
class BookmarkCountUseCase @Inject constructor(
    private val repo: ProductRepo,
) : BaseRoomUseCase<Unit, Int>() {

    override fun run(param: Unit): BaseRoomUseCase<Unit, Int> {
        launch {
            repo.selectBookmarkCount().collect {
                execute(
                    it
                )
            }
        }
        return this
    }
}