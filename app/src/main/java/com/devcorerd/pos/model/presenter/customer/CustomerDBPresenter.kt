package com.devcorerd.pos.model.presenter.customer

import android.content.Context
import com.devcorerd.pos.model.entity.Customer
import com.devcorerd.pos.model.presenter.CustomerPresenter
import com.devcorerd.pos.model.table.CustomerTable
import ru.arturvasilov.sqlite.rx.RxSQLite

/**
 * @author Ing. Wilson Garcia
 * Created on 8/11/18
 */
class CustomerDBPresenter(private val context: Context?) : CustomerPresenter(context, null) {

    constructor() : this(null)

    fun addCustomer(customer: Customer, successCallback: () -> Unit,
                    errorCallback: (error: Throwable) -> Unit) {

        RxSQLite.get().insert(CustomerTable.TABLE, customer).subscribe({
            successCallback()
        }, { error: Throwable ->
            errorCallback(error)
        }).dispose()
    }

    override fun getCustomers(successCallback: (customers: MutableList<Customer>) -> Unit,
                              errorCallback: (error: Throwable) -> Unit) {

        RxSQLite.get().query(CustomerTable.TABLE).subscribe({ customers: MutableList<Customer>? ->
            successCallback(customers!!)
        }, { error: Throwable ->
            error.printStackTrace()
            errorCallback(error)
        }).dispose()

    }

}