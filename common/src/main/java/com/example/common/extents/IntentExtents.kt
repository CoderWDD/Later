package com.example.common.extents

import android.content.Intent
import android.os.Parcelable


inline fun <reified T : Parcelable> Intent.getParcelableArrayListFromData(
    key: String?,
    clazz: Class<out T>
): ArrayList<T>? {
    if (extras == null) return null
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        extras?.getParcelableArrayList(key, clazz)
    } else {
        extras?.getParcelableArrayList(key)
    }
}