package com.devcorerd.pos.core.ocr

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.google.android.gms.vision.text.TextBlock

/**
 * @author Ing. Wilson Garcia
 * Created on 1/14/18
 */
class OcrImageGraphic(text: TextBlock?) {

    private var rectPaint: Paint? = null
    var text: TextBlock? = text

    init {
        if (rectPaint == null) {
            rectPaint = Paint()
            rectPaint!!.color = Color.BLUE
            rectPaint!!.style = Paint.Style.STROKE
            rectPaint!!.strokeWidth = 8.0f
        }
    }

    fun draw(canvas: Canvas) {
        if (text == null) {
            return
        }

        val rect = RectF(text!!.boundingBox)
        canvas.drawRect(rect, rectPaint)
    }
}