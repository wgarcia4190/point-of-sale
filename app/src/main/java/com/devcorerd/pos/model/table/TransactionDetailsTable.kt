package com.devcorerd.pos.model.table

import android.content.ContentValues
import android.database.Cursor
import com.devcorerd.pos.model.entity.TransactionDetails
import org.sqlite.database.sqlite.SQLiteDatabase
import ru.arturvasilov.sqlite.core.BaseTable
import ru.arturvasilov.sqlite.core.Table
import ru.arturvasilov.sqlite.utils.TableBuilder

/**
 * @author Ing. Wilson Garcia
 * Created on 8/2/18
 */
class TransactionDetailsTable : BaseTable<TransactionDetails>() {

    companion object {
        val TABLE: Table<TransactionDetails> = TransactionDetailsTable()
        lateinit var database: SQLiteDatabase

        const val transactionId = "transactionId"
        const val productName = "productName"
        const val productSku = "productSku"
        const val productPrice = "productPrice"
        const val quantity = "quantity"
    }

    override fun onCreate(database: SQLiteDatabase) {
        TransactionDetailsTable.database = database

        TableBuilder.create(this)
                .intColumn(transactionId)
                .textColumn(productName)
                .textColumn(productSku)
                .realColumn(productPrice)
                .intColumn(quantity)
                .execute(database)
    }

    override fun fromCursor(cursor: Cursor): TransactionDetails {
        val transactionId = cursor.getInt(cursor.getColumnIndex(transactionId))
        val productName = cursor.getString(cursor.getColumnIndex(productName))
        val productSku = cursor.getString(cursor.getColumnIndex(productSku))
        val productPrice = cursor.getDouble(cursor.getColumnIndex(productPrice))
        val quantity = cursor.getInt(cursor.getColumnIndex(quantity))

        return TransactionDetails(transactionId, productName, productSku, productPrice, quantity)
    }

    override fun toValues(transaction: TransactionDetails): ContentValues {
        val values = ContentValues()
        values.put(transactionId, transaction.transactionId)
        values.put(productName, transaction.productName)
        values.put(productSku, transaction.productSku)
        values.put(productPrice, transaction.productPrice)
        values.put(quantity, transaction.quantity)

        return values
    }


}