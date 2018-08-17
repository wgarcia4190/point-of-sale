package com.devcorerd.pos.model.presenter.customer

import android.content.Context
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.model.entity.Customer
import com.devcorerd.pos.model.presenter.CustomerPresenter
import com.devcorerd.pos.model.service.CustomerService
import org.json.JSONObject
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * @author Ing. Wilson Garcia
 * Created on 8/11/18
 */
class CustomerServicePresenter(private val context: Context?,
                               private var mainService:
                               CustomerService? = CustomerService(context)) : CustomerPresenter(context, mainService) {

    override fun getCustomers(successCallback: (customers: MutableList<Customer>) -> Unit, errorCallback: (error: Throwable) -> Unit) {
        manageSubscription(subscription = mainService!!.API.getCustomers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val jsonObject = JSONObject(it.body().string())
                    val customersArray = jsonObject.optJSONArray("items")
                    val customers = mutableListOf<Customer>()

                    for (index in 0 until customersArray.length()) {
                        val customerJson = customersArray.getJSONObject(index)
                        val fullName = customerJson.optString("nombre")
                                .split(" ")

                        val customer = Customer(fullName[0], fullName[1],
                                customerJson.optString("rnc"), "", "")
                        customers.add(customer)
                    }

                    addCustomers(customers)
                    successCallback(customers)

                    UIHelper.hideLoadingDialog()
                }, { error: Throwable ->
                    error.printStackTrace()
                    UIHelper.hideLoadingDialog()
                    errorCallback(error)
                }), showLoading = true)
    }

}