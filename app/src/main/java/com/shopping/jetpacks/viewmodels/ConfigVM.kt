package com.shopping.jetpacks.viewmodels

import androidx.lifecycle.ViewModel
import com.shopping.base.BaseViewModel
import com.shopping.jetpacks.repo.impls.ConfigRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * [ConfigVM] is a [ViewModel] of Assessment Operations
 *
 * [repo] : [ConfigRepoImpl] repo class link with [ConfigRepoImpl]
 *
 */
@HiltViewModel
class ConfigVM @Inject constructor(val repo: ConfigRepoImpl) : BaseViewModel() {

    fun fetchString(type: Any): String {
        return repo.fetchString(type)
    }

}
