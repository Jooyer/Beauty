package com.meirenmeitu.ui.define

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.meirenmeitu.ui.R

/** https://www.jianshu.com/p/cb42e75711f1
 * Desc: 自定义 仿 IOS 菊花
 * Author: Jooyer
 * Date: 2018-09-15
 * Time: 22:37
 */
class ChrysanthemumView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private lateinit var rectF: RectF

    private var paint = Paint()

    private var radius = 0 //半径

    private var count = 0

    private var run = true //动画控制

    constructor(context: Context) : this(context,null)

    init {
        initialize()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(radius * 2, radius * 2)
    }

    private fun initialize() {
        paint = Paint()
        paint.isAntiAlias = true
        paint.color = ContextCompat.getColor(context, R.color.color_write)
        radius = dp2px(15F).toInt()
        rectF = RectF((radius - dp2px(1.2F)),  dp2px(0F),
                (radius +  dp2px(1.2F)), dp2px(8F))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.rotate((count * 30).toFloat(), radius.toFloat(), radius.toFloat())
        for (i in 0 until 12) {
            paint.alpha = 255 - i * 20
            canvas.drawRoundRect(rectF, 10f, 10f, paint)
            canvas.rotate(30f, radius.toFloat(), radius.toFloat())
        }
        count++
        if (count > 12) {
            count = 0
        }
        if (run) {
            postInvalidateDelayed(100)
        }
    }

    private fun dp2px(def: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, def, context.resources.displayMetrics)
    }

    fun start() {
        if (!run) {
            postInvalidateDelayed(100)
            run = true
        }
    }

    fun stop() {
        run = false
    }
}
