package com.shopping.jetpacks.retorfit

import com.shopping.BuildConfig
import com.shopping.jetpacks.entities.ProductResponse
import retrofit2.Call
import retrofit2.http.*

/**
 * Configure and Customizing [RetrofitCalls]
 */
interface RetrofitCalls {

    /**
     * Api call to fetch list of products
     *
     * @param url Link to load information : [Urls.fetchCustom]
     * @return [ProductResponse] response
     */
    @GET(BuildConfig.API_URL+"v3/2f06b453-8375-43cf-861a-06e95a951328")
    fun fetchProduct(): Call<ProductResponse>

}
