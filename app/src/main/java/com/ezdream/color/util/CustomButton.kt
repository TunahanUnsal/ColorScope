package com.ezdream.color.util// CustomButton.kt

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class CustomButton : AppCompatButton {

    private var paint: Paint = Paint()

    constructor(context: Context) : super(context) {
        setupPaint()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setupPaint()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        setupPaint()
    }

    private fun setupPaint() {
        paint.apply {
            color = Color.BLACK // Çizgi rengi
            style = Paint.Style.STROKE
            strokeWidth = 5f // Çizgi kalınlığı
            isAntiAlias = true
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            pathEffect = null
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            paint
        )
    }
}
