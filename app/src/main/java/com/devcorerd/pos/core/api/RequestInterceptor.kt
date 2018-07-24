package com.devcorerd.pos.core.api

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by wgarcia on 4/25/2018.
 */
class RequestInterceptor(val context: Context) : Interceptor {

    private val tag: String = "RequestInterceptor"

    override fun intercept(chain: Interceptor.Chain?): Response {
        Log.d(tag, "-----------------------------------------START REQUEST--------------------------------------------------")

        val original = chain!!.request()
        val requestBuilder = original.newBuilder()

        Log.d(tag, original.toString())
        Log.d(tag, "-----------------------------------------END REQUEST--------------------------------------------------")
        return chain.proceed(requestBuilder.build())
    }

}