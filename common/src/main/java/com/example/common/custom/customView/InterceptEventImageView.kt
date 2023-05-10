package com.example.common.custom.customView

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

class InterceptEventImageView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0): androidx.appcompat.widget.AppCompatImageView(context, attributeSet, defStyleAttr) {

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 处理点击事件
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 当手指按下时，改变 ImageView 的透明度以表示已选中
                alpha = 0.5f
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // 当手指抬起或手势被取消时，恢复 ImageView 的透明度
                alpha = 1.0f
            }
        }
        // 返回 true，表示拦截点击事件，不向其父容器传递
        return true
    }
}