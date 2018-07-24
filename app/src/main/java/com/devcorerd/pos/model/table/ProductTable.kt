package com.devcorerd.pos.model.table

import android.content.ContentValues
import android.database.Cursor
import com.devcorerd.pos.helper.DateHelper
import com.devcorerd.pos.model.entity.Product
import org.joda.time.DateTime
import org.sqlite.database.sqlite.SQLiteDatabase
import ru.arturvasilov.sqlite.core.BaseTable
import ru.arturvasilov.sqlite.core.Table
import ru.arturvasilov.sqlite.utils.TableBuilder


/**
 * @author Ing. Wilson Garcia
 * Created on 7/23/18
 */
class ProductTable : BaseTable<Product>() {

    companion object {
        val TABLE: Table<Product> = ProductTable()

        const val sku = "sku"
        const val name = "name"
        const val description = "description"
        const val category = "category"
        const val soldBy = "soldBy"
        const val price = "price"
        const val barcode = "barcode"
        const val representation = "representation"
        const val hasImage = "hasImage"
        const val isFavorite = "isFavorite"
        const val creationDate = "creationDate"
        const val modificationDate = "modificationDate"
    }



    override fun onCreate(database: SQLiteDatabase) {
        TableBuilder.create(this)
                .textColumn(sku)
                .textColumn(name)
                .textColumn(description)
                .textColumn(category)
                .textColumn(soldBy)
                .realColumn(price)
                .textColumn(barcode)
                .textColumn(representation)
                .intColumn(hasImage)
                .intColumn(isFavorite)
                .textColumn(creationDate)
                .textColumn(modificationDate)
                .execute(database)
    }

    override fun fromCursor(cursor: Cursor): Product {
        val sku = cursor.getString(cursor.getColumnIndex(sku))
        val name = cursor.getString(cursor.getColumnIndex(name))
        val description = cursor.getString(cursor.getColumnIndex(description))
        val category = cursor.getString(cursor.getColumnIndex(category))
        val soldBy = cursor.getString(cursor.getColumnIndex(soldBy))
        val price = cursor.getDouble(cursor.getColumnIndex(price))
        val barcode = cursor.getString(cursor.getColumnIndex(barcode))
        val representation = cursor.getString(cursor.getColumnIndex(representation))
        val hasImage = cursor.getInt(cursor.getColumnIndex(hasImage)) != 0
        val isFavorite = cursor.getInt(cursor.getColumnIndex(isFavorite)) != 0
        val creationDate = cursor.getString(cursor.getColumnIndex(creationDate))
        val modificationDate = cursor.getString(cursor.getColumnIndex(modificationDate))
        return Product(name, description, category, soldBy.single(), price, sku, barcode,
                representation, hasImage, isFavorite, DateHelper.getStringAsDate(creationDate),
                DateHelper.getStringAsDate(modificationDate))
    }

    override fun toValues(product: Product): ContentValues {
        val values = ContentValues()
        values.put(sku, product.sku)
        values.put(name, product.name)
        values.put(description, product.description)
        values.put(category, product.category)
        values.put(soldBy, product.soldBy.toString())
        values.put(price, product.price)
        values.put(barcode, product.barcode)
        values.put(representation, product.representation)
        values.put(hasImage, product.hasImage)
        values.put(isFavorite, product.isFavorite)
        values.put(creationDate, DateHelper.getDateAsString(product.creationDate))
        values.put(modificationDate, DateHelper.getDateAsString(product.modificationDate))

        return values
    }

}