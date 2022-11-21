package com.shopping.jetpacks.source.network.impl


import com.shopping.jetpacks.entities.Text
import com.shopping.jetpacks.retorfit.RetrofitUtil
import com.shopping.jetpacks.source.network.face.ConfigRemote
import com.shopping.utilities.GetData
import com.shopping.utilities.Utils
import com.shopping.variables.enums.GetString
import com.shopping.variables.enums.ToolbarTitle
import org.json.JSONObject
import retrofit2.Call
import javax.inject.Inject

class ConfigRemoteSourceImpl @Inject constructor() : ConfigRemote {
    private var apiCall: Call<Any>? = null

    /**
     * Fetch Sharing message from Remote config
     */
    override fun fetchString(type: Any): Text {
        try {
            var value = ""
            val jsonObject = when (type) {
                is GetString -> {
                    value = type.value
                    JSONObject(
                        GetData.assetJSONFile("get_strings.json")
                    )
                }
                is ToolbarTitle -> {
                    value = type.value
                    JSONObject(
                        GetData.assetJSONFile("toolbar_title.json")
                    )
                }
                else -> JSONObject()
            }
            if (Utils.keyCheck(jsonObject, value))
                return RetrofitUtil.instance.fromJson(
                    jsonObject.getString(value),
                    Text::class.java
                )
        } catch (e: Exception) {
//            FirebaseCrashlytics.getInstance().log(e.message ?: "")
        }
        return Text()
    }

    override fun cancelApiCalls() {
        apiCall?.cancel()
    }
}