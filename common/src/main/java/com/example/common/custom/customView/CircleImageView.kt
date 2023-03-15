package com.example.common.custom.customView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : AppCompatImageView(context, attrs, defStyleAttr) {
    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val drawable = drawable as? BitmapDrawable ?: return
        val bitmap = drawable.bitmap ?: return

        val minSize = width.coerceAtMost(height)
        val radius = minSize / 2.0f

        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader

        canvas.drawCircle(radius, radius, radius, paint)
    }
}
