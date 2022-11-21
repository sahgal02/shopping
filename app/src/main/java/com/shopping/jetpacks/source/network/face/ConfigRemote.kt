package com.shopping.jetpacks.source.network.face

import com.shopping.base.BaseRemotePattern
import com.shopping.jetpacks.entities.Text

interface ConfigRemote : BaseRemotePattern {

    fun fetchString(type: Any): Text
}