@file:Suppress("unused")
package gr.blackswamp.core.extensions

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes

fun Context.themeColor(@AttrRes attrRes: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrRes, typedValue, true)
    return typedValue.data
}

fun Context.getAttrValue(@AttrRes attr: Int): Int? {
    return try {
        val typedValue = TypedValue()
        theme.resolveAttribute(attr, typedValue, true)
        typedValue.data
    } catch (ignored: Throwable) {
        null
    }
}