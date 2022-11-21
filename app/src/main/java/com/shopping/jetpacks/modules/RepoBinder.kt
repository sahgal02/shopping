package com.shopping.jetpacks.modules

import com.shopping.jetpacks.repo.face.CartRepo
import com.shopping.jetpacks.repo.face.ConfigRepo
import com.shopping.jetpacks.repo.face.ProductRepo
import com.shopping.jetpacks.repo.impls.CartRepoImpl
import com.shopping.jetpacks.repo.impls.ConfigRepoImpl
import com.shopping.jetpacks.repo.impls.ProductRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepoBinder {

    @Binds
    abstract fun provideProductRepo(impl: ProductRepoImpl): ProductRepo

    @Binds
    abstract fun provideConfigRepo(impl: ConfigRepoImpl): ConfigRepo

    @Binds
    abstract fun provideCartRepo(impl: CartRepoImpl): CartRepo
}