package com.shopping.base

import androidx.lifecycle.ViewModel

/**
 * Base class implementation for [ViewModel] common surfaces
 */
open class BaseViewModel(private vararg val baseUserCase: BaseUseCase<*,*>) :
    ViewModel() {
    /**
     * By default on cleared cancelling the [BaseUseCase.closeProcess]
     */
    override fun onCleared() {
        super.onCleared()
        baseUserCase.map {
            it.closeProcess()
        }
    }

    /**
     * Function structure to close process call
     */
    open fun cancelProcess() {}
}