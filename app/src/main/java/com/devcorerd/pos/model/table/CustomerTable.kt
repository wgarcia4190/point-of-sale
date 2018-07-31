package com.devcorerd.pos.model.table

import android.content.ContentValues
import android.database.Cursor
import com.devcorerd.pos.model.entity.Customer
import org.sqlite.database.sqlite.SQLiteDatabase
import ru.arturvasilov.sqlite.core.BaseTable
import ru.arturvasilov.sqlite.core.Table
import ru.arturvasilov.sqlite.utils.TableBuilder

/**
 * @author Ing. Wilson Garcia
 * Created on 7/24/18
 */
class CustomerTable : BaseTable<Customer>() {

    companion object {
        val TABLE: Table<Customer> = CustomerTable()
        lateinit var database: SQLiteDatabase

        const val name = "name"
        const val lastName = "lastName"
        const val socialID = "socialID"
        const val email = "email"
    }


    override fun onCreate(database: SQLiteDatabase) {
        CustomerTable.database = database

        TableBuilder.create(this)
                .textColumn(name)
                .textColumn(lastName)
                .textColumn(socialID)
                .textColumn(email)
                .primaryKey(socialID)
                .execute(database)
    }

    override fun fromCursor(cursor: Cursor): Customer {
        val name = cursor.getString(cursor.getColumnIndex(name))
        val lastName = cursor.getString(cursor.getColumnIndex(lastName))
        val phone = cursor.getString(cursor.getColumnIndex(socialID))
        val email = cursor.getString(cursor.getColumnIndex(email))

        return Customer(name, lastName, phone, email)
    }

    override fun toValues(customer: Customer): ContentValues {
        val values = ContentValues()
        values.put(name, customer.name)
        values.put(lastName, customer.lastName)
        values.put(socialID, customer.socialID)
        values.put(email, customer.email)

        return values
    }

}