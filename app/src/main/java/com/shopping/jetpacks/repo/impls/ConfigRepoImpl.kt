package com.shopping.jetpacks.repo.impls

import com.shopping.jetpacks.repo.face.ConfigRepo
import com.shopping.jetpacks.source.network.face.ConfigRemote
import com.shopping.utilities.MyApplication
import com.shopping.variables.interfaces.LanguageKeys
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [ConfigRepoImpl] is a Repo Class will do every calculation and link transaction either api based or database based
 * For more details & links check [ConfigVM]
 *
 * @param remoteSource [ConfigRemoteSourceImpl] for Apis Transactions
 */
@Singleton
class ConfigRepoImpl @Inject constructor(
    private val remoteSource: ConfigRemote
) : ConfigRepo {

    override fun fetchString(type: Any): String {
        return if (MyApplication.languageId == LanguageKeys.LANG_ENGLISH)
            remoteSource.fetchString(type).en ?: ""
        else
            remoteSource.fetchString(type).hi ?: ""
    }
}