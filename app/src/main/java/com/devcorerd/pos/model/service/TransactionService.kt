package com.devcorerd.pos.model.service

import android.content.Context
import com.devcorerd.pos.core.api.APICore
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query
import rx.Observable

/**
 * @author Ing. Wilson Garcia
 * Created on 8/2/18
 */
class TransactionService(context: Context?, baseUrl: String, storeHeaders: Boolean) :
        APICore(context, baseUrl, storeHeaders) {

    constructor(context: Context?) : this(context, "http://trebolerp.com/apex/adess/data/", true)

    val API: TransactionAPI by lazy {
        retrofit!!.create(TransactionAPI::class.java)
    }

    interface TransactionAPI {
        @POST("encabft")
        fun createHeader(@Query("PCLIENTE") client: String,
                         @Query("PVENDEDOR") seller: String,
                         @Query("PFECHA") date: String,
                         @Query("PFACTURA") invoiceId: Int): Observable<Response<ResponseBody>>

        @POST("detaft")
        fun createDetail(@Query("PUNIDAD") unit: String,
                         @Query("PPRODUCTO") sku: String,
                         @Query("PFACTURA") invoiceId: Int,
                         @Query("PPRECIO") price: Double,
                         @Query("PCANTIDAD") quantity: Int,
                         @Query("PVENDEDOR") seller: String,
                         @Query("PFINAL") final: String): Observable<Response<ResponseBody>>
    }

}