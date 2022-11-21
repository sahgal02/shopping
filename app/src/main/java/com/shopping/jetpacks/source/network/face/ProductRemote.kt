package com.shopping.jetpacks.source.network.face

import com.shopping.base.BaseRemotePattern
import com.shopping.jetpacks.entities.ProductResponse
import com.shopping.jetpacks.extensions.ResourceState


interface ProductRemote : BaseRemotePattern {

    suspend fun apiFetchProducts(): ResourceState<ProductResponse>

}
