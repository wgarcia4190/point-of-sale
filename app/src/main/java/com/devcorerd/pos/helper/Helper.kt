package com.devcorerd.pos.helper

import android.content.Context
import android.net.ConnectivityManager

/**
 * @author Ing. Wilson Garcia
 * Created on 7/16/18
 */
class Helper private constructor() {

    companion object {
        fun isInternetAvailable(context: Context): Boolean {
            try {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                return cm.activeNetworkInfo != null
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            return false
        }
    }
    
}