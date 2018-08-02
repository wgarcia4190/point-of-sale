package com.devcorerd.pos.model.table

import android.content.ContentValues
import android.database.Cursor
import com.devcorerd.pos.helper.DateHelper
import com.devcorerd.pos.model.entity.Printer
import org.sqlite.database.sqlite.SQLiteDatabase
import ru.arturvasilov.sqlite.core.BaseTable
import ru.arturvasilov.sqlite.core.Table
import ru.arturvasilov.sqlite.utils.TableBuilder

/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
class PrinterTable : BaseTable<Printer>() {


    companion object {
        val TABLE: Table<Printer> = PrinterTable()
        lateinit var database: SQLiteDatabase

        const val name = "name"
        const val device = "device"
        const val address = "address"
        const val size = "size"
        const val creationDate = "creationDate"
        const val modificationDate = "modificationDate"
    }


    override fun onCreate(database: SQLiteDatabase) {
        CategoryTable.database = database

        TableBuilder.create(this)
                .textColumn(name)
                .textColumn(device)
                .textColumn(address)
                .textColumn(size)
                .textColumn(creationDate)
                .textColumn(modificationDate)
                .primaryKey(name)
                .execute(database)
    }

    override fun fromCursor(cursor: Cursor): Printer {
        val name = cursor.getString(cursor.getColumnIndex(name))
        val device = cursor.getString(cursor.getColumnIndex(device))
        val address = cursor.getString(cursor.getColumnIndex(address))
        val size = cursor.getString(cursor.getColumnIndex(size))
        val creationDate = cursor.getString(cursor.getColumnIndex(creationDate))
        val modificationDate = cursor.getString(cursor.getColumnIndex(modificationDate))

        return Printer(name, device, address, size, DateHelper.getStringAsDate(creationDate),
                DateHelper.getStringAsDate(modificationDate))
    }

    override fun toValues(printer: Printer): ContentValues {
        val values = ContentValues()
        values.put(name, printer.name)
        values.put(device, printer.device)
        values.put(address, printer.address)
        values.put(size, printer.size)
        values.put(creationDate, DateHelper.getDateAsString(printer.creationDate))
        values.put(modificationDate, DateHelper.getDateAsString(printer.modificationDate))

        return values
    }

}