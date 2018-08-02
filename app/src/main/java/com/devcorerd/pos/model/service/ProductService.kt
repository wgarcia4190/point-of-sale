package com.devcorerd.pos.model.service

import android.content.Context
import com.devcorerd.pos.core.api.APICore
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * @author Ing. Wilson Garcia
 * Created on 7/23/18
 */
class ProductService(context: Context?, baseUrl: String, storeHeaders: Boolean):
        APICore(context, baseUrl, storeHeaders){

    constructor(context: Context?): this(context, "http://trebolerp.com/apex/adess/data/", true)

    val API: ProductAPI by lazy {
        retrofit!!.create(ProductAPI::class.java)
    }

    interface ProductAPI {
        @GET("productos")
        fun getProducts(): Observable<Response<ResponseBody>>
    }

}