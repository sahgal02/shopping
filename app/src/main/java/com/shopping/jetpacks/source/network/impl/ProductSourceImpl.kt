package com.shopping.jetpacks.source.network.impl

import com.shopping.jetpacks.entities.ProductResponse
import com.shopping.jetpacks.extensions.mapToEntity
import com.shopping.jetpacks.retorfit.RetrofitCalls
import com.shopping.jetpacks.source.network.face.ProductRemote
import com.shopping.jetpacks.extensions.ResourceState
import retrofit2.Call
import javax.inject.Inject

class ProductSourceImpl @Inject constructor(
    private val retrofitCalls: RetrofitCalls
) : ProductRemote {
    private lateinit var apiDataCall: Call<ProductResponse>

    override suspend fun apiFetchProducts(): ResourceState<ProductResponse> {
        apiDataCall = retrofitCalls.fetchProduct()
        return apiDataCall.mapToEntity {
            it
        }
    }

    override fun cancelApiCalls() {
        if (this::apiDataCall.isInitialized)
            apiDataCall.cancel()
    }
}