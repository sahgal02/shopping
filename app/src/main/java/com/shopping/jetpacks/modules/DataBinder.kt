package com.shopping.jetpacks.modules

import com.shopping.jetpacks.source.data.face.CartData
import com.shopping.jetpacks.source.data.face.ProductData
import com.shopping.jetpacks.source.data.impl.CartDataImpl
import com.shopping.jetpacks.source.data.impl.ProductDataImpl
import com.shopping.jetpacks.source.network.face.*
import com.shopping.jetpacks.source.network.impl.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DataBinder {

    @Binds
    abstract fun provideProductData(impl: ProductDataImpl): ProductData


    @Binds
    abstract fun provideCartData(impl: CartDataImpl): CartData
}