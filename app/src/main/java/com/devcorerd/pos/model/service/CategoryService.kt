package com.devcorerd.pos.model.service

import android.content.Context
import com.devcorerd.pos.core.api.APICore
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Created by wgarcia on 7/26/2018.
 */
class CategoryService(context: Context?, baseUrl: String, storeHeaders: Boolean):
        APICore(context, baseUrl, storeHeaders){

    constructor(context: Context?): this(context, "http://127.0.0.1/", true)

    val API: CustomerAPI by lazy {
        retrofit!!.create(CustomerAPI::class.java)
    }

    interface CustomerAPI {
        @GET("/rest/v2/all")
        fun validateToken(@Query("token") token: String): Observable<ResponseBody>
    }

}