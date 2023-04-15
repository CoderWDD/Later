package com.example.laterlist.callback

interface MenuItemDialogClickCallBack<T> {
    fun onConfirmClickListener(content: T)
    fun onCancelClickListener()
}