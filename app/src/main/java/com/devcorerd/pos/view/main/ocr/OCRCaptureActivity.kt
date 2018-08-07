@file:Suppress("DEPRECATION")

package com.devcorerd.pos.view.main.ocr

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.devcorerd.pos.R
import com.devcorerd.pos.core.camera.CameraSource
import com.devcorerd.pos.core.camera.GraphicOverlay
import com.devcorerd.pos.core.ocr.OcrDetectorProcessor
import com.devcorerd.pos.core.ocr.OcrGraphic
import com.devcorerd.pos.core.ocr.OcrImageDetectorProcessor
import com.devcorerd.pos.core.ui.ActivityBase
import com.devcorerd.pos.helper.BitmapHelper
import com.devcorerd.pos.helper.ConstantsHelper
import com.devcorerd.pos.helper.Helper
import com.devcorerd.pos.helper.UIHelper
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import kotlinx.android.synthetic.main.ocr_activity.*
import java.io.IOException

/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
class OCRCaptureActivity : ActivityBase(R.layout.ocr_activity) {

    private val rotation: Int = 90
    private val offset: Int = 0

    private var cameraSource: CameraSource? = null
    private var textRecognizer: TextRecognizer? = null
    private var textBlocks: MutableList<TextBlock> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupEvents()
        if (!Helper.checkPermissions(this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), ConstantsHelper.cameraCode)
        } else
            createCameraSource(true)
    }

    private fun setupEvents() {
        takePicture.setOnClickListener {
            cameraSource!!.takePicture(null) { b: ByteArray? ->
                val bmp: Bitmap = BitmapHelper
                        .rotate(BitmapFactory.decodeByteArray(b, offset, b!!.size), rotation)
                readImageText(bmp)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {

            ConstantsHelper.cameraCode -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    UIHelper.showMessage(this, "Error",
                            getString(R.string.not_file_permission_granted))
                else
                    createCameraSource(true)

            }
        }
    }

    private fun createCameraSource(autoFocus: Boolean) {
        val context: Context = applicationContext

        textRecognizer = TextRecognizer.Builder(context).build()!!
        textRecognizer!!.setProcessor(OcrDetectorProcessor(graphicOverlay
                as GraphicOverlay<OcrGraphic>))
        if (!textRecognizer!!.isOperational) {

            val lowStorageFilter = IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW)
            val hasLowStorage: Boolean = registerReceiver(null, lowStorageFilter) != null

            if (hasLowStorage) {
                Toast.makeText(this, "Poco Almacenamiento", Toast.LENGTH_LONG).show()
            }
        }

        cameraSource = CameraSource.Builder(context, textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setRequestedFps(8.0f)
                .setFocusMode(if (autoFocus) Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE else null)
                .build()
    }

    private fun readImageText(bmp: Bitmap) {
        cameraSourcePreview.stop()

        if (textRecognizer != null && textRecognizer!!.isOperational) {
            OcrImageDetectorProcessor
                    .process(bmp, textRecognizer!!, textBlocks)

            var blocks = ""

            (0 until textBlocks.size)
                    .asSequence()
                    .map { textBlocks[it] }
                    .forEach { blocks = blocks.plus(it.value).plus("\n").plus("\n") }

            textBlocks.clear()

            val intent = Intent()
            intent.putExtra("scan_results", blocks)

            setResult(9000, intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    @Throws(SecurityException::class)
    private fun startCameraSource() {
        val code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                applicationContext)
        if (code != ConnectionResult.SUCCESS) {
            val dlg = GoogleApiAvailability.getInstance().getErrorDialog(this, code, 9001)
            dlg.show()
        }

        if (cameraSource != null) {
            try {
                cameraSourcePreview!!.start(cameraSource!!, graphicOverlay!!)
            } catch (e: IOException) {
                cameraSource!!.release()
                cameraSource = null
            }

        }
    }

    override fun onResume() {
        super.onResume()
        startCameraSource()
    }

    override fun onPause() {
        super.onPause()
        if (cameraSourcePreview != null) {
            cameraSourcePreview!!.stop()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (cameraSourcePreview != null) {
            cameraSourcePreview!!.release()
        }
    }
}