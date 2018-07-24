package com.devcorerd.pos.core.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by wgarcia on 4/25/2018.
 */
class ResponseInterceptor(private val storeHeader: Boolean) : Interceptor {

    private val tag: String = "ResponseInterceptor"

    override fun intercept(chain: Interceptor.Chain?): Response {
        Log.d(tag, "-----------------------------------------START RESPONSE--------------------------------------------------")

        val originalResponse = chain!!.proceed(chain.request())
        android.util.Log.d(tag, originalResponse.code().toString())

        Log.d(tag, "-----------------------------------------END RESPONSE--------------------------------------------------")
        return originalResponse
    }
}

