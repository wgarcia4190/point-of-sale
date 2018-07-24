package com.devcorerd.pos.core.api

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by wgarcia on 4/25/2018.
 */
open class APICore(context: Context?, baseUrl: String, storeHeader: Boolean) {
    protected var retrofit: Retrofit? = null
        private set

    val gSon: Gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .serializeNulls()
            .create()

    constructor(context: Context) : this(context, "http://127.0.0.1/", true)
    constructor(context: Context, storeHeader: Boolean) : this(context, "http://127.0.0.1/",
            storeHeader)

    init {
        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getUnsafeOkHttpClient(context!!, storeHeader))
                .addConverterFactory(ConverterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gSon))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
    }

    private fun getUnsafeOkHttpClient(context: Context, storeHeader: Boolean): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
                .followRedirects(false)
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .addInterceptor(RequestInterceptor(context))
                .addInterceptor(ResponseInterceptor(storeHeader))
                .addInterceptor(interceptor).build()
    }

}