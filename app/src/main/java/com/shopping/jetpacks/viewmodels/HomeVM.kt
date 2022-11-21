package com.shopping.jetpacks.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.shopping.base.BaseViewModel
import com.shopping.jetpacks.usecase.BookmarkCountUseCase
import com.shopping.jetpacks.usecase.CartCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    val useCase: CartCountUseCase,
    private val bookmarkCountUseCase: BookmarkCountUseCase
) : BaseViewModel() {

    /**
     * Fetch number of items from cart
     */
    val cartCountLiveData = Transformations.switchMap(
        useCase.getData()
    ) {
        val liveData = MutableLiveData<Int>()
        liveData.postValue(it)
        liveData
    }

    /**
     * Fetch number of items from cart
     */
    val bookmarkCountLiveData = Transformations.switchMap(
        bookmarkCountUseCase.getData()
    ) {
        val liveData = MutableLiveData<Int>()
        liveData.postValue(it)
        liveData
    }

    fun fetchBookmarkCount() = bookmarkCountUseCase.run(Unit)


    fun fetchCount() = useCase.run(Unit)
}