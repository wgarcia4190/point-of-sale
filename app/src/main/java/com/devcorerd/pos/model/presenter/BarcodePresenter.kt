package com.devcorerd.pos.model.presenter

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.devcorerd.pos.core.api.Presenter
import com.devcorerd.pos.model.service.BarcodeService
import okhttp3.ResponseBody
import org.json.JSONObject
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * @author Ing. Wilson Garcia
 * Created on 7/25/18
 */
class BarcodePresenter(private val context: Context?,
                       private var mainService:
                       BarcodeService? = BarcodeService(context)) : Presenter() {

    constructor() : this(null, null)

    override fun initService(context: AppCompatActivity?) {
        super.initService(context)
        mainService = BarcodeService(context)
    }

    fun getProductInformation(upcCode: String, signature: String,
                              successCallback: (productJson: JSONObject) -> Unit,
                              errorCallback: (error: Throwable) -> Unit){
        manageSubscription(subscription = mainService!!.API.getProductInformation(upcCode, signature)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({response: ResponseBody ->
                    successCallback(JSONObject(response.string()))
                }, { error: Throwable ->
                    error.printStackTrace()
                }), showLoading = false)
    }
}