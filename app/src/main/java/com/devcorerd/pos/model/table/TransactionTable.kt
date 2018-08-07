package com.devcorerd.pos.model.table

import android.content.ContentValues
import android.database.Cursor
import com.devcorerd.pos.helper.DateHelper
import com.devcorerd.pos.model.entity.Transaction
import org.sqlite.database.sqlite.SQLiteDatabase
import ru.arturvasilov.sqlite.core.BaseTable
import ru.arturvasilov.sqlite.core.Table
import ru.arturvasilov.sqlite.utils.TableBuilder

/**
 * @author Ing. Wilson Garcia
 * Created on 8/2/18
 */
class TransactionTable : BaseTable<Transaction>() {

    companion object {
        val TABLE: Table<Transaction> = TransactionTable()
        lateinit var database: SQLiteDatabase

        const val id = "id"
        const val cashier = "cashier"
        const val paymentMethod = "paymentMethod"
        const val total = "total"
        const val amount = "amount"
        const val voucher = "voucher"
        const val customer = "customer"
        const val creationDate = "creationDate"
        const val modificationDate = "modificationDate"
    }

    override fun onCreate(database: SQLiteDatabase) {
        TransactionTable.database = database

        TableBuilder.create(this)
                .intColumn(id)
                .textColumn(cashier)
                .textColumn(paymentMethod)
                .realColumn(total)
                .realColumn(amount)
                .textColumn(voucher)
                .textColumn(customer)
                .textColumn(creationDate)
                .textColumn(modificationDate)
                .primaryKey(id)
                .execute(database)
    }

    override fun fromCursor(cursor: Cursor): Transaction {
        val id = cursor.getInt(cursor.getColumnIndex(id))
        val cashier = cursor.getString(cursor.getColumnIndex(cashier))
        val paymentMethod = cursor.getString(cursor.getColumnIndex(paymentMethod))
        val total = cursor.getDouble(cursor.getColumnIndex(total))
        val amount = cursor.getDouble(cursor.getColumnIndex(amount))
        val voucher = cursor.getString(cursor.getColumnIndex(voucher))
        val customer = cursor.getString(cursor.getColumnIndex(customer))
        val creationDate = cursor.getString(cursor.getColumnIndex(creationDate))
        val modificationDate = cursor.getString(cursor.getColumnIndex(modificationDate))

        return Transaction(id, cashier, paymentMethod, total, amount, voucher, customer,
                DateHelper.getStringHourAsDate(creationDate),
                DateHelper.getStringHourAsDate(modificationDate))
    }

    override fun toValues(transaction: Transaction): ContentValues {
        val values = ContentValues()
        values.put(id, transaction.id)
        values.put(cashier, transaction.cashier)
        values.put(paymentMethod, transaction.paymentMethod)
        values.put(total, transaction.total)
        values.put(amount, transaction.amount)
        values.put(voucher, transaction.voucher)
        values.put(customer, transaction.customer)
        values.put(creationDate, DateHelper.getDateHourAsString(transaction.creationDate))
        values.put(modificationDate, DateHelper.getDateHourAsString(transaction.modificationDate))

        return values
    }
}