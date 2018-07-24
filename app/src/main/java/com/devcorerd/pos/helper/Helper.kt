package com.devcorerd.pos.helper

import android.content.Context
import android.net.ConnectivityManager
import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.util.Base64


/**
 * @author Ing. Wilson Garcia
 * Created on 7/16/18
 */
class Helper private constructor() {

    companion object {
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

        fun getBitmapFromString(encodedImage: String): Bitmap{
            val decodedString = Base64.decode(encodedImage, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedString, 0,
                    decodedString.size)
        }
    }
    
}