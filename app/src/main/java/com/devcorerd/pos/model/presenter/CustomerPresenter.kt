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
abstract class CustomerPresenter(private val context: Context?,
                                 private var mainService:
                                 CustomerService? = CustomerService(context)) : Presenter(){


    override fun initService(context: AppCompatActivity?) {
        super.initService(context)
        mainService = CustomerService(context)
    }

    fun addCustomers(customers: MutableList<Customer>) {
        RxSQLite.get().insert(CustomerTable.TABLE, customers).subscribe().dispose()
    }

    abstract fun getCustomers(successCallback: (customers: MutableList<Customer>) -> Unit,
                     errorCallback: (error: Throwable) -> Unit)
}