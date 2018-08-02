package com.devcorerd.pos.core.ocr

import android.util.Log
import android.util.SparseArray
import com.devcorerd.pos.core.camera.GraphicOverlay
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.text.TextBlock

/**
 * @author Ing. Wilson Garcia
 * Created on 1/14/18
 */
class OcrDetectorProcessor(ocrGraphicOverlay: GraphicOverlay<OcrGraphic>): Detector.Processor<TextBlock> {
    private val graphicOverlay: GraphicOverlay<OcrGraphic> = ocrGraphicOverlay
    private val tag: String = "Processor"

    override fun release() {
        graphicOverlay.clear()
    }

    override fun receiveDetections(detections: Detector.Detections<TextBlock>?) {
        graphicOverlay.clear()

        val items: SparseArray<TextBlock>? = detections!!.detectedItems
        for(index in 0 until items!!.size()){
            val item: TextBlock? = items.valueAt(index)

            if(item != null && item.value != null)
                Log.d(tag, "Text Detected! ".plus(item.value))
            graphicOverlay.add(OcrGraphic(graphicOverlay, item))
        }
    }
}