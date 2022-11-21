package com.shopping.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


/**
 * [NetworkUseCase] class creating [CoroutineScope] mechanism using [Job] to provide cancel abilities,
 * We are here using [Dispatchers.IO] to run all work load outside [Dispatchers.Main] thread
 */
abstract class NetworkUseCase<P> : CoroutineScope, UseCase() {

    protected var job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    abstract fun run(params: P? = null): NetworkUseCase<P>

    /**
     * Call to cancel [Job]
     */
    fun cancel() {
        job.cancel()
        checkJob()
    }

    /**
     * Check is [Job] cancelled
     */
    fun isCancelled(): Boolean {
        return job.isCancelled
    }

    /**
     * Check is [Job] cancelled, if cancelled. Create New one
     */
    private fun checkJob() {
        if (job.isCancelled) {
            job = Job()
        }
    }
}
