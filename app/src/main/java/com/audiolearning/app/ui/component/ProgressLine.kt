package com.audiolearning.app.ui.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.audiolearning.app.R
import kotlin.math.roundToInt

class ProgressLine(context: Context, attrs: AttributeSet) : View(context, attrs) {
    var max: Int = 1
    private var lineColor: Int = 0
    private var paint = Paint()
    private var rect = Rect(0, 0, 100, height)

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ProgressLine, 0, 0)
        try {
            lineColor = a.getColor(R.styleable.ProgressLine_lineColor, 0)
        } finally {
            a.recycle()
        }

        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true
        paint.color = lineColor
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawRect(rect, paint)
    }

    var progress = 0
        set(value) {
            field = value

            val percentage = value.toFloat() / max
            val rectWidth = (this.width * percentage).roundToInt()

            rect.set(0, 0, rectWidth, height)

            invalidate()
            requestLayout()
        }
}
