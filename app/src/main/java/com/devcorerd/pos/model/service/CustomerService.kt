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
 * Created on 7/24/18
 */
class CustomerService(context: Context?, baseUrl: String, storeHeaders: Boolean):
        APICore(context, baseUrl, storeHeaders){

    constructor(context: Context?): this(context, "http://trebolerp.com/apex/adess/data/", true)

    val API: CustomerAPI by lazy {
        retrofit!!.create(CustomerAPI::class.java)
    }

    interface CustomerAPI {
        @GET("clientes")
        fun getCustomers(): Observable<Response<ResponseBody>>
    }

}