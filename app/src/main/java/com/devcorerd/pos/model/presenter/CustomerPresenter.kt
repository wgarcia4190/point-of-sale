package com.devcorerd.pos.model.presenter

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.devcorerd.pos.core.api.Presenter
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.model.entity.Customer
import com.devcorerd.pos.model.service.CustomerService
import com.devcorerd.pos.model.table.CustomerTable
import org.json.JSONObject
import ru.arturvasilov.sqlite.rx.RxSQLite
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * @author Ing. Wilson Garcia
 * Created on 7/24/18
 */
class CustomerPresenter(private val context: Context?,
                        private var mainService:
                        CustomerService? = CustomerService(context)) : Presenter() {

    constructor() : this(null, null)

    override fun initService(context: AppCompatActivity?) {
        super.initService(context)
        mainService = CustomerService(context)
    }

    fun addCustomer(customer: Customer, successCallback: () -> Unit,
                    errorCallback: (error: Throwable) -> Unit) {

        RxSQLite.get().insert(CustomerTable.TABLE, customer).subscribe({
            successCallback()
        }, { error: Throwable ->
            errorCallback(error)
        }).dispose()
    }

    fun addCustomers(customers: MutableList<Customer>) {
        RxSQLite.get().insert(CustomerTable.TABLE, customers).subscribe().dispose()
    }

    fun getCustomers(successCallback: (customers: MutableList<Customer>) -> Unit,
                     errorCallback: (error: Throwable) -> Unit) {

        RxSQLite.get().query(CustomerTable.TABLE).subscribe({ customers: MutableList<Customer>? ->
            successCallback(customers!!)
        }, { error: Throwable ->
            error.printStackTrace()
            errorCallback(error)
        }).dispose()

    }

    fun getCustomersFromServer(successCallback: (customers: MutableList<Customer>) -> Unit,
                               errorCallback: (error: Throwable) -> Unit) {

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