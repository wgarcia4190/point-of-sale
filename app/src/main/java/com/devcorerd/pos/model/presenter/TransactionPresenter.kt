package com.devcorerd.pos.model.presenter

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.devcorerd.pos.core.api.Presenter
import com.devcorerd.pos.helper.DateHelper
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.model.entity.Printer
import com.devcorerd.pos.model.entity.Section
import com.devcorerd.pos.model.entity.Transaction
import com.devcorerd.pos.model.entity.TransactionDetails
import com.devcorerd.pos.model.service.TransactionService
import com.devcorerd.pos.model.table.PrinterTable
import com.devcorerd.pos.model.table.TransactionDetailsTable
import com.devcorerd.pos.model.table.TransactionTable
import ru.arturvasilov.sqlite.core.Where
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
                }, { error: Throwable ->
                    error.printStackTrace()
                    errorCallback(error)
                }), showLoading = false)
    }

    fun addTransaction(transaction: Transaction, successCallback: () -> Unit,
                       errorCallback: (error: Throwable) -> Unit) {

        RxSQLite.get().insert(TransactionTable.TABLE, transaction).subscribe({
            RxSQLite.get().insert(TransactionDetailsTable.TABLE, transaction.transactionDetails!!).subscribe({
                successCallback()
            }, { subError: Throwable ->
                errorCallback(subError)
            }).dispose()
        }, { error: Throwable ->
            errorCallback(error)
        }).dispose()
    }

    fun getTransactions(successCallback: (transactions: MutableList<Transaction>) -> Unit,
                        errorCallback: (error: Throwable) -> Unit) {
        RxSQLite.get().query(TransactionTable.TABLE).subscribe({
            successCallback(it)
        }, { error: Throwable ->
            error.printStackTrace()
            errorCallback(error)
        }).dispose()
    }

    fun getTransactionsPerSection(successCallback: (transactions: MutableList<Section<Transaction>>) -> Unit,
                                  errorCallback: (error: Throwable) -> Unit) {
        RxSQLite.get().query(TransactionTable.TABLE).subscribe({
            val sections: MutableList<Section<Transaction>> = mutableListOf()
            val sectionsMap: MutableMap<String, MutableList<Transaction>> = mutableMapOf()
            for (transaction in it) {
                val dateString = DateHelper.getDateAsLongString(transaction.creationDate)
                if (!sectionsMap.containsKey(dateString)) {
                    sectionsMap[dateString] = mutableListOf()
                }
                sectionsMap[dateString]!!.add(transaction)
            }
            for (entry in sectionsMap.entries) {
                sections.add(Section(entry.key, entry.value))
            }
            successCallback(sections)
        }, { error: Throwable ->
            error.printStackTrace()
            errorCallback(error)
        }).dispose()
    }

    fun getTransactionsDetails(transactionId: Int,
                               successCallback: (transactions: MutableList<TransactionDetails>) -> Unit,
                               errorCallback: (error: Throwable) -> Unit) {
        RxSQLite.get().query(TransactionDetailsTable.TABLE,
                Where.create().equalTo(TransactionDetailsTable.transactionId, transactionId)).subscribe({
            successCallback(it)
        }, { error: Throwable ->
            error.printStackTrace()
            errorCallback(error)
        }).dispose()
    }
}