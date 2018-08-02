package com.devcorerd.pos.model.presenter

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.devcorerd.pos.core.api.Presenter
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.model.entity.Printer
import com.devcorerd.pos.model.service.TransactionService
import com.devcorerd.pos.model.table.PrinterTable
import ru.arturvasilov.sqlite.rx.RxSQLite
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
class TransactionPresenter(private val context: Context?,
                           private var mainService:
                           TransactionService? = TransactionService(context)) : Presenter() {

    constructor() : this(null, null)

    override fun initService(context: AppCompatActivity?) {
        super.initService(context)
        mainService = TransactionService(context)
    }

    fun addPrinter(printer: Printer, successCallback: () -> Unit,
                   errorCallback: (error: Throwable) -> Unit) {

        RxSQLite.get().insert(PrinterTable.TABLE, printer).subscribe({
            successCallback()
        }, { error: Throwable ->
            errorCallback(error)
        }).dispose()
    }

    fun getPrinter(successCallback: (printer: Printer?) -> Unit,
                   errorCallback: (error: Throwable) -> Unit) {

        RxSQLite.get().query(PrinterTable.TABLE).subscribe({ printers: MutableList<Printer>? ->
            if (printers != null && !printers.isEmpty())
                successCallback(printers[0])
            else
                successCallback(null)
        }, { error: Throwable ->
            error.printStackTrace()
            errorCallback(error)
        }).dispose()
    }

    fun createInvoiceHeader(client: String, seller: String, date: String, invoiceId: Int,
                            successCallback: () -> Unit, errorCallback: (error: Throwable) -> Unit) {

        manageSubscription(subscription = mainService!!.API.createHeader(client, seller, date, invoiceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({


                    successCallback()
                    UIHelper.hideLoadingDialog()
                }, { error: Throwable ->
                    error.printStackTrace()
                    UIHelper.hideLoadingDialog()
                    errorCallback(error)
                }), showLoading = true)

    }

    fun createInvoiceDetail(unit: String, sku: String, invoiceId: Int, price: Double,
                            quantity: Int, seller: String, final: String, successCallback: () -> Unit,
                            errorCallback: (error: Throwable) -> Unit) {

        manageSubscription(subscription = mainService!!.API.createDetail(unit, sku, invoiceId, price, quantity, seller, final)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    successCallback()
                    UIHelper.hideLoadingDialog()
                }, { error: Throwable ->
                    error.printStackTrace()
                    errorCallback(error)
                }), showLoading = false)
    }
}