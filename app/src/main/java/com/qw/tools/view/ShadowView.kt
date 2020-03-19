
import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.qw.tools.R


/**
 * 阴影view类
 *
 * @author cd5160866
 */
class ShadowView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {

    private val mShadowPaint: Paint

    private var cornerRadius: Float = 0f

    private var shadowSizeStart: Float = 0f

    private var shadowSizeEnd: Float = 0f

    private var shadowSizeTop: Float = 0f

    private var shadowSizeBottom: Float = 0f

    private var mDefaultColor: Int = 0

    private var mPressColor: Int = 0

    private var isClicked: Boolean = false

    companion object {
        /**
         * 指定一个view的类名和 属性直接生成带阴影的view
         */
        @JvmStatic
        fun replaceOriginWithShadowView(context: Context, viewAttrs: AttributeSet, viewClass: Class<out View>?): ShadowView {
            val shadowView = ShadowView(context)
            var view: View
            val params = shadowView.generateLayoutParams(viewAttrs) as LayoutParams
            params.setMargins(0, 0, 0, 0)
            if (params.width == 0) {
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
            }
            if (params.height == 0) {
                params.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            try {
                view = viewClass!!.getConstructor(Context::class.java, AttributeSet::class.java).newInstance(context, viewAttrs)
            } catch (e: Exception) {
                view = TextView(context, viewAttrs)
                e.printStackTrace()
            }

            shadowView.addView(view, params)
            return shadowView
        }
    }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ShadowView)
        mDefaultColor = a.getColor(R.styleable.ShadowView_ShadowDefaultColor, Color.TRANSPARENT)
        mPressColor = a.getColor(R.styleable.ShadowView_ShadowPressedColor, Color.TRANSPARENT)
        shadowSizeStart = a.getDimensionPixelOffset(R.styleable.ShadowView_ShadowSizeStart, 0).toFloat()
        shadowSizeEnd = a.getDimensionPixelOffset(R.styleable.ShadowView_ShadowSizeEnd, 0).toFloat()
        shadowSizeTop = a.getDimensionPixelOffset(R.styleable.ShadowView_ShadowSizeTop, 0).toFloat()
        shadowSizeBottom = a.getDimensionPixelOffset(R.styleable.ShadowView_ShadowSizeBottom, 0).toFloat()
        cornerRadius = a.getDimensionPixelOffset(R.styleable.ShadowView_ShadowCornerRadius, 0).toFloat()
        a.recycle()
        mShadowPaint = Paint()
        mShadowPaint.style = Paint.Style.FILL
        mShadowPaint.isAntiAlias = true
        mShadowPaint.color = mDefaultColor
        setPaintShadowRadius(mShadowPaint, shadowSizeStart)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    fun setShadowSize(left: Int, top: Int, right: Int, bottom: Int) {
        if (left.toFloat() == this.shadowSizeStart && top.toFloat() == this.shadowSizeTop && right.toFloat() == this.shadowSizeEnd && bottom.toFloat() == this.shadowSizeBottom) {
            return
        }
        this.shadowSizeStart = left.toFloat()
        this.shadowSizeEnd = right.toFloat()
        this.shadowSizeTop = top.toFloat()
        this.shadowSizeBottom = bottom.toFloat()
        requestLayout()
    }

    fun setStatusColors(defaultColor: Int, pressColor: Int) {
        if (mDefaultColor == defaultColor && mPressColor == pressColor) {
            return
        }
        this.mDefaultColor = defaultColor
        this.mPressColor = pressColor
        updateShadowColor(mDefaultColor)
    }

    fun setShadowRadius(radius: Float) {
        if (cornerRadius == radius) {
            return
        }
        this.cornerRadius = radius
        requestLayout()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val dispatch = super.dispatchTouchEvent(ev)
        if (dispatch) {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> updatePressStatus(true)
                MotionEvent.ACTION_UP -> updatePressStatus(false)
                else -> {
                }
            }
        }
        return dispatch
    }

    private fun updatePressStatus(isClick: Boolean) {
        if (mPressColor == 0 || mDefaultColor == 0) {
            return
        }
        if (isClicked == isClick) {
            return
        }
        isClicked = isClick
        updateShadowColor(if (isClick) mPressColor else mDefaultColor)
    }

    private fun updateShadowColor(color: Int) {
        mShadowPaint.color = color
        setPaintShadowRadius(mShadowPaint, shadowSizeStart)
    }

    private fun setPaintShadowRadius(paint: Paint?, radius: Float) {
        if (radius <= 0 || paint == null) {
            return
        }
        paint.maskFilter = BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL)
    }

    override fun dispatchDraw(canvas: Canvas) {
        val child = getChildAt(0)
        val left = child.left
        val top = child.top
        val right = child.right
        val bottom = child.bottom
        val drawablePath = Path()
        drawablePath.moveTo(left + cornerRadius, top.toFloat())
        drawablePath.arcTo(RectF(left.toFloat(), top.toFloat(), left + 2 * cornerRadius, top + 2 * cornerRadius), -90f, -90f, false)
        drawablePath.lineTo(left.toFloat(), bottom - cornerRadius)
        drawablePath.arcTo(RectF(left.toFloat(), bottom - 2 * cornerRadius, left + 2 * cornerRadius, bottom.toFloat()), 180f, -90f, false)
        drawablePath.lineTo(right - cornerRadius, bottom.toFloat())
        drawablePath.arcTo(RectF(right - 2 * cornerRadius, bottom - 2 * cornerRadius, right.toFloat(), bottom.toFloat()), 90f, -90f, false)
        drawablePath.lineTo(right.toFloat(), top - cornerRadius)
        drawablePath.arcTo(RectF(right - 2 * cornerRadius, top.toFloat(), right.toFloat(), top + 2 * cornerRadius), 0f, -90f, false)
        drawablePath.close()
        canvas.drawPath(drawablePath, mShadowPaint)
        super.dispatchDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        val height: Int
        val width: Int
        val widthMeasureSpecSize: Int
        val heightMeasureSpecSize: Int
        val child = getChildAt(0)
        val childLayoutParams = child.layoutParams as LayoutParams
        if (childLayoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            widthMeasureSpecSize = View.MeasureSpec.getSize(widthMeasureSpec) - (shadowSizeStart + shadowSizeEnd).toInt()
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(widthMeasureSpecSize, View.MeasureSpec.EXACTLY)
        }
        if (childLayoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            heightMeasureSpecSize = View.MeasureSpec.getSize(heightMeasureSpec) - (shadowSizeBottom + shadowSizeTop).toInt()
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(heightMeasureSpecSize, View.MeasureSpec.EXACTLY)
        }
        measureChild(child, widthMeasureSpec, heightMeasureSpec)
        val childHeight = child.measuredHeight
        val childWidth = child.measuredWidth
        width = (childWidth.toFloat() + shadowSizeStart + shadowSizeEnd).toInt()
        height = (childHeight.toFloat() + shadowSizeTop + shadowSizeBottom).toInt()
        setMeasuredDimension(width, height)
    }

    class LayoutParams : ViewGroup.MarginLayoutParams {

        constructor(c: Context, attrs: AttributeSet) : super(c, attrs)

        constructor(width: Int, height: Int) : super(width, height)

        constructor(source: ViewGroup.LayoutParams) : super(source)
    }

    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams {
        return LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): ViewGroup.LayoutParams {
        return LayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val child = getChildAt(0)
        val childMeasureWidth = child.measuredWidth
        val childMeasureHeight = child.measuredHeight
        child.layout(shadowSizeStart.toInt(), shadowSizeTop.toInt(), (shadowSizeStart + childMeasureWidth).toInt(), (shadowSizeTop + childMeasureHeight).toInt())
    }


}

