package com.shopping.jetpacks.extensions

import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.shopping.jetpacks.providers.getString
import com.shopping.utilities.elements.CircularImageView
import com.shopping.variables.enums.GetString
import com.shopping.variables.enums.ToolbarTitle

fun ImageView.loadImage(url: String?, placeholder: Int) {
    url?.let {
        Glide.with(this.context).load(it)
            .placeholder(placeholder)
            .into(this)
    }
}

fun CircularImageView.loadImage(url: String?, placeholder: Int) {
    url?.let {
        Glide.with(this.context).load(it)
            .centerCrop()
            .into(this)
    }
}

fun MenuItem.setString(text: GetString) {
    this.title = getString(text)
    this.titleCondensed = getString(text)
}

/**
 * [Any] can be [GetString], [ToolbarTitle], [String]
 */
fun TextView.setString(id: Any?, prefix: String? = "", extra: String? = "") {
    this.text =
        "${if (prefix.isNullOrEmpty()) "" else "$prefix "}${getString(id)}${if (extra.isNullOrEmpty()) "" else " $extra"}"
}
