package com.example.common.extents

import android.graphics.Rect
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import java.lang.Math.abs

inline fun View.onChildViewClick(
    vararg layoutId: String, // View的子控件Id（若输入多个则表示多个控件所组成的并集矩形区域）
    x: Float, // 触点横坐标
    y: Float,// 触点纵坐标
    clickAction: ((View?) -> Unit) // 子控件点击响应事件
) {
    var clickedView: View? = null
    // 遍历所有子控件id
    layoutId
        .map { id ->
            // 根据id查找出子控件实例
            find<View>(id)?.let { view ->
                // 获取子控件相对于父控件的矩形区域
                view.getRelativeRectTo(this).also { rect ->
                    // 如果矩形区域包含触点则表示子控件被点击（记录被点击的子控件）
                    if (rect.contains(x.toInt(), y.toInt())) {
                        clickedView = view
                    }
                }
            } ?: Rect()
        }
        // 将所有子控件矩形区域做并集
        .fold(Rect()) { init, rect -> init.apply { union(rect) } }
        // 如果并集中包含触摸点，则表示并集所对应的大矩形区域被点击
        .takeIf { it.contains(x.toInt(), y.toInt()) }
        ?.let { clickAction.invoke(clickedView) }
}


fun View.getRelativeRectTo(otherView: View): Rect {
    val parentRect = Rect().also { otherView.getGlobalVisibleRect(it) }
    val childRect = Rect().also { getGlobalVisibleRect(it) }
    // 将 2个 Rect 做相对运算后返回一个新的 Rect
    return childRect.relativeTo(parentRect)
}

// Rect 相对运算（可以理解为将坐标原点进行平移）
fun Rect.relativeTo(otherRect: Rect): Rect {
    val relativeLeft = left - otherRect.left
    val relativeTop = top - otherRect.top
    val relativeRight = relativeLeft + right - left
    val relativeBottom = relativeTop + bottom - top
    return Rect(relativeLeft, relativeTop, relativeRight, relativeBottom)
}

fun <T : View> View.find(id: String): T = findViewById<T>(id.toLayoutId())

fun <T : View> AppCompatActivity.find(id: String): T = findViewById<T>(id.toLayoutId())


fun String.toLayoutId(): Int {
    var id = hashCode()
    val parent_id = "0"
    if (this == parent_id) id = 0
    return kotlin.math.abs(id)
}