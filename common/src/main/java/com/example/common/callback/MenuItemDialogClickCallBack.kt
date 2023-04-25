package com.example.common.callback

interface MenuItemDialogClickCallBack<T> {
    fun onConfirmClickListener(content: T)
    fun onCancelClickListener()
}