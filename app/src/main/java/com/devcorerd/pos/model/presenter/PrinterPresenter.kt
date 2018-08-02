package com.devcorerd.pos.model.presenter

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.devcorerd.pos.core.api.Presenter
import com.devcorerd.pos.model.entity.Printer
import com.devcorerd.pos.model.service.PrinterService
import com.devcorerd.pos.model.table.PrinterTable
import ru.arturvasilov.sqlite.core.Where
import ru.arturvasilov.sqlite.rx.RxSQLite

/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
class PrinterPresenter(private val context: Context?,
                       private var mainService:
                       PrinterService? = PrinterService(context)) : Presenter() {

    constructor() : this(null, null)

    override fun initService(context: AppCompatActivity?) {
        super.initService(context)
        mainService = PrinterService(context)
    }

    fun addPrinter(printer: Printer, successCallback: () -> Unit,
                   errorCallback: (error: Throwable) -> Unit) {

        RxSQLite.get().insert(PrinterTable.TABLE, printer).subscribe({
            successCallback()
        }, { error: Throwable ->
            errorCallback(error)
        }).dispose()
    }

    fun updatePrinter(printer: Printer, successCallback: () -> Unit,
                      errorCallback: (error: Throwable) -> Unit) {

        RxSQLite.get().update(PrinterTable.TABLE, Where.create().equalTo(PrinterTable.name, printer.name), printer).subscribe({
            successCallback()
        }, { error: Throwable ->
            errorCallback(error)
        }).dispose()
    }

    fun getPrinters(successCallback: (printers: MutableList<Printer>) -> Unit,
                    errorCallback: (error: Throwable) -> Unit) {

        RxSQLite.get().query(PrinterTable.TABLE).subscribe({ printers: MutableList<Printer>? ->
            successCallback(printers!!)
        }, { error: Throwable ->
            error.printStackTrace()
            errorCallback(error)
        }).dispose()
    }

    fun deletePrinter(printer: Printer, successCallback: () -> Unit,
                      errorCallback: (error: Throwable) -> Unit) {

        RxSQLite.get().delete(PrinterTable.TABLE, Where.create().equalTo(PrinterTable.name,
                printer.name)).subscribe({
            successCallback()
        }, { error: Throwable ->
            error.printStackTrace()
            errorCallback(error)
        })
    }
}