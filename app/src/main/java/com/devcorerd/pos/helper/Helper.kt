package com.devcorerd.pos.helper

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.os.Build
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.SignatureException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


/**
 * @author Ing. Wilson Garcia
 * Created on 7/16/18
 */
class Helper private constructor() {

    companion object {
        private val HMAC_SHA1_ALGORITHM = "HmacSHA1"
        fun isInternetAvailable(context: Context): Boolean {
            try {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                        as ConnectivityManager
                return cm.activeNetworkInfo != null
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            return false
        }

        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE)
                    as InputMethodManager
            var view = activity.currentFocus
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun getBitmapFromString(encodedImage: String): Bitmap {
            val decodedString = Base64.decode(encodedImage, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedString, 0,
                    decodedString.size)
        }

        fun checkPermissions(context: Context, vararg permissions: String): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                for (permission in permissions) {
                    if (context.checkSelfPermission(permission) !=
                            PackageManager.PERMISSION_GRANTED)
                        return false
                }
                return true
            }
            return true
        }

        fun getFileAsBase64(file: File): String? {
            var encodedFile: String? = null
            try {
                val fileInputStreamReader = FileInputStream(file)
                val bytes = ByteArray(file.length().toInt())
                fileInputStreamReader.read(bytes)
                encodedFile = Base64.encodeToString(bytes, Base64.NO_WRAP)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return encodedFile
        }

        fun getBitmapAsBase64(bitmap: Bitmap): String {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        }

        private fun toHexString(bytes: ByteArray): String {
            val formatter = Formatter()

            for (b in bytes) {
                formatter.format("%02x", b)
            }

            return formatter.toString()
        }

        @Throws(SignatureException::class, NoSuchAlgorithmException::class, InvalidKeyException::class)
        fun computeHash(data: String): ByteArray? {
            val signingKey = SecretKeySpec("Fc15R0b2b6Fu4Mq5".toByteArray(), HMAC_SHA1_ALGORITHM)
            val mac = Mac.getInstance(HMAC_SHA1_ALGORITHM)
            mac.init(signingKey)
            return mac.doFinal(data.toByteArray())
        }
    }

}