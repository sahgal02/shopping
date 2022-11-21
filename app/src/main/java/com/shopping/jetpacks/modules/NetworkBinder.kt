package com.shopping.jetpacks.modules

import com.shopping.jetpacks.source.network.face.*
import com.shopping.jetpacks.source.network.impl.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class NetworkBinder {

    @Binds
    abstract fun provideProductRemote(impl: ProductSourceImpl): ProductRemote

    @Binds
    abstract fun provideConfigRemote(impl: ConfigRemoteSourceImpl): ConfigRemote

}