package com.shopping.jetpacks.repo.face

interface ConfigRepo {
    fun fetchString(type: Any): String
}