package com.devcorerd.pos.core.ocr

import android.graphics.Bitmap
import android.util.Log
import android.util.SparseArray
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer

/**
 * @author Ing. Wilson Garcia
 * Created on 1/14/18
 */
class OcrImageDetectorProcessor {

    companion object {
        private val tag: String = "OcrImageDetectorProcessor"

        fun process(bmp: Bitmap?, detector: TextRecognizer, textBlocks: MutableList<TextBlock>):
                MutableList<OcrImageGraphic> {
            val graphics: MutableList<OcrImageGraphic> = arrayListOf()

            val frame: Frame = Frame.Builder().setBitmap(bmp!!).build()
            val items: SparseArray<TextBlock>? = detector.detect(frame)
            for (index in 0 until items!!.size()) {
                val item: TextBlock? = items.valueAt(index)

                if (item != null && item.value != null)
                    Log.d(tag, "Text Detected! ".plus(item.value))
                graphics.add(OcrImageGraphic(item))
                item?.let { textBlocks.add(it) }
            }

            return graphics
        }
    }
}