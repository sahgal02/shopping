package com.shopping.jetpacks.providers

import android.widget.*
import androidx.databinding.BindingAdapter
import com.shopping.utilities.MyApplication
import com.shopping.variables.enums.GetString
import com.shopping.variables.enums.ToolbarTitle

/**
 * Binding adapter for more ease text insertion directly based on [GetString]
 */
@BindingAdapter("text")
fun text(view: TextView, id: GetString) {
    view.text = getString(id)
}

@BindingAdapter("hint")
fun hint(view: TextView, id: GetString) {
    view.hint = getString(id)
}

@BindingAdapter("text")
fun text(view: TextView, id: ToolbarTitle) {
    view.text = getString(id)
}

@BindingAdapter("button")
fun buttonText(view: Button, id: GetString) {
    view.text = getString(id)
}

@BindingAdapter("contentDescription")
fun imageContentText(view: ImageView, id: GetString) {
    view.contentDescription = getString(id)
}

@BindingAdapter("description")
fun imageDescription(view: ImageButton, id: GetString) {
    view.contentDescription = getString(id)
}

fun getString(id: Any?): String {
    return when (id) {
        is GetString -> {
            MyApplication.configRepo.fetchString(id)
        }
        is ToolbarTitle -> MyApplication.configRepo.fetchString(id)
        is Double -> id.toString()
        is Int -> {
            try {
                MyApplication.instance.getString(id)
            } catch (e : Exception){
                ""
            }
        }
        is String -> id
        else -> ""
    }
}