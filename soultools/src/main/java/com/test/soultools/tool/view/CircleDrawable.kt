package com.test.soultools.tool.view

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

/**
 * @author cd5160866
 * @date 2018/11/28
 */
class CircleDrawable
/**
 * @param color       气泡主色
 * @param diameter    直径
 * @param strokeColor 描边颜色
 * @param strokeWidth 描边宽度
 */
@JvmOverloads constructor(private val color: Int, private val diameter: Int, private val strokeColor: Int = 0, private val strokeWidth: Int = 0) : Drawable() {

    private val mPaint: Paint = Paint()

    init {
        mPaint.isAntiAlias = true
    }

    override fun draw(canvas: Canvas) {

        if (strokeWidth > 0) {
            mPaint.color = strokeColor
            canvas.drawCircle((diameter / 2).toFloat(), (diameter / 2).toFloat(), (diameter / 2).toFloat(), mPaint)
            mPaint.color = color
            canvas.drawCircle((diameter / 2).toFloat(), (diameter / 2).toFloat(), (diameter / 2 - strokeWidth).toFloat(), mPaint)
        } else {
            mPaint.color = color
            canvas.drawCircle((diameter / 2).toFloat(), (diameter / 2).toFloat(), (diameter / 2).toFloat(), mPaint)
        }
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        mPaint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun getIntrinsicHeight(): Int {
        return diameter
    }

    override fun getIntrinsicWidth(): Int {
        return diameter
    }
}
