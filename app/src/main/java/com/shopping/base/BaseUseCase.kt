package com.shopping.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shopping.jetpacks.extensions.ResourceState

abstract class BaseUseCase<P, R>(vararg val remotes : BaseRemotePattern) : NetworkUseCase<P>() {
    private val liveData by lazy { MutableLiveData<R>() }

    fun execute(parameter: R) {
        liveData.postValue(parameter)
    }

    open fun closeProcess() {
        for (remote in remotes){
            remote.cancelApiCalls()
        }
        cancel()
    }

    fun getData() = liveData
}

/**
 * Manage api call to get response from [BaseUseCase]
 */
fun <T> LifecycleOwner.observe(liveData: LiveData<T>?, action: (t: T) -> Unit) {
    liveData?.observe(this) { it?.let { t -> action(t) } }
}
