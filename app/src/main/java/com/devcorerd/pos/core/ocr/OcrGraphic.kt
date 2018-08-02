package com.devcorerd.pos.core.ocr

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import com.devcorerd.pos.core.camera.GraphicOverlay
import com.google.android.gms.vision.text.TextBlock

/**
 * @author Ing. Wilson Garcia
 * Created on 1/14/18
 */
class OcrGraphic(mOverlay: GraphicOverlay<*>, text: TextBlock?) : GraphicOverlay.Graphic(mOverlay) {

    private var id: Int = 0
    private var rectPaint: Paint? = null
    private var text: TextBlock? = text

    init {
        if (rectPaint == null) {
            rectPaint = Paint()
            rectPaint!!.color = Color.BLUE
            rectPaint!!.style = Paint.Style.STROKE
            rectPaint!!.strokeWidth = 0.0f
        }

        postInvalidate()
    }

    override fun draw(canvas: Canvas) {
        if (text == null) {
            return
        }

        val rect = RectF(text!!.boundingBox)
        rect.left = translateX(rect.left)
        rect.top = translateY(rect.top)
        rect.right = translateX(rect.right)
        rect.bottom = translateY(rect.bottom)
        canvas.drawRect(rect, rectPaint)
    }

    override fun contains(x: Float, y: Float): Boolean {
        if (text == null) {
            return false
        }
        val rect = RectF(text!!.boundingBox)
        rect.left = translateX(rect.left)
        rect.top = translateY(rect.top)
        rect.right = translateX(rect.right)
        rect.bottom = translateY(rect.bottom)
        return rect.left < x && rect.right > x && rect.top < y && rect.bottom > y
    }

}