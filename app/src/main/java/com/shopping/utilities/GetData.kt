package com.shopping.utilities

import java.io.InputStream

/**
 * [GetData] provide hardcoded JSON, Values like : [com.shopping.variables.enums.GetString]
 */
object GetData {

    fun assetJSONFile(fileName: String): String {
        val file: InputStream = MyApplication.instance.applicationContext.assets.open(fileName)
        val formArray = ByteArray(file.available())
        file.read(formArray)
        file.close()
        return String(formArray)
    }
}