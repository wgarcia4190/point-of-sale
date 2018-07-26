package com.devcorerd.pos.model.service

import android.content.Context
import com.devcorerd.pos.core.api.APICore
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * @author Ing. Wilson Garcia
 * Created on 7/25/18
 */
class BarcodeService(context: Context?, baseUrl: String, storeHeaders: Boolean) :
        APICore(context, baseUrl, storeHeaders) {

    constructor(context: Context?) : this(context, "https://www.digit-eyes.com/gtin/", true)

    val API: BarcodeAPI by lazy {
        retrofit!!.create(BarcodeAPI::class.java)
    }

    interface BarcodeAPI {
        @GET("v2_0")
        fun getProductInformation(@Query("upcCode") upcCode: String,
                                  @Query("signature") signature: String,
                                  @Query("field_names") fieldName: String = "all",
                                  @Query("language") language: String = "es",
                                  @Query("app_key") appKey: String = "/8alsFhhfx8m"): Observable<ResponseBody>
    }

}