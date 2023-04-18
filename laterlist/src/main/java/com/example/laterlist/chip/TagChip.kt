package com.example.laterlist.chip

import android.content.Context
import android.util.AttributeSet
import com.example.laterlist.R
import com.google.android.material.chip.Chip

class TagChip @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = com.google.android.material.R.attr.chipStyle): Chip(context, attrs, defStyleAttr) {

    init {
        setOnCloseIconClickListener {
            // todo 删除标签
        }
        // 设置标签的样式
        isChipIconVisible = false
        chipIcon = context.getDrawable(R.drawable.tag_icon)
        isCloseIconVisible = true
    }
}