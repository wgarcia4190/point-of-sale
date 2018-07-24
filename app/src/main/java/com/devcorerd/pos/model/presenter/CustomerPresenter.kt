package com.devcorerd.pos.model.presenter

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.devcorerd.pos.core.api.Presenter
import com.devcorerd.pos.model.entity.Customer
import com.devcorerd.pos.model.service.CustomerService
import com.devcorerd.pos.model.table.CustomerTable
import ru.arturvasilov.sqlite.rx.RxSQLite

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

    fun getCustomers(successCallback: (customers: MutableList<Customer>) -> Unit,
                     errorCallback: (error: Throwable) -> Unit) {

        RxSQLite.get().query(CustomerTable.TABLE).subscribe({ customers: MutableList<Customer>? ->
            successCallback(customers!!)
        }, { error: Throwable ->
            error.printStackTrace()
            errorCallback(error)
        }).dispose()

    }

}