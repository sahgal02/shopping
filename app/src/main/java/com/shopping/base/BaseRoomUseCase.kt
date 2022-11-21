package com.shopping.base

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseRoomUseCase<P, R>: CoroutineScope, UseCase() {
    private val liveData by lazy { MutableLiveData<R>() }

    protected var job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    abstract fun run(param : P): BaseRoomUseCase<P, R>

    fun cancel() {
        job.cancel()
    }

    fun execute(parameter: R) {
        liveData.postValue(parameter)
    }

    fun getData() = liveData
}