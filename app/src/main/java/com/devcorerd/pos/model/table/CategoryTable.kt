package com.devcorerd.pos.model.table

import android.content.ContentValues
import android.database.Cursor
import com.devcorerd.pos.helper.DateHelper
import com.devcorerd.pos.model.entity.Category
import org.sqlite.database.sqlite.SQLiteDatabase
import ru.arturvasilov.sqlite.core.BaseTable
import ru.arturvasilov.sqlite.core.Table
import ru.arturvasilov.sqlite.utils.TableBuilder

/**
 * Created by wgarcia on 7/26/2018.
 */
class CategoryTable : BaseTable<Category>() {



    companion object {
        val TABLE: Table<Category> = CategoryTable()
        lateinit var database: SQLiteDatabase

        const val name = "name"
        const val color = "color"
        const val totalItems = "totalItems"
        const val creationDate = "creationDate"
        const val modificationDate = "modificationDate"
    }


    override fun onCreate(database: SQLiteDatabase) {
        CategoryTable.database = database

        TableBuilder.create(this)
                .textColumn(name)
                .textColumn(color)
                .intColumn(totalItems)
                .textColumn(creationDate)
                .textColumn(modificationDate)
                .primaryKey(name)
                .execute(database)
    }

    override fun fromCursor(cursor: Cursor): Category {
        val name = cursor.getString(cursor.getColumnIndex(name))
        val color = cursor.getString(cursor.getColumnIndex(color))
        val totalItems = cursor.getInt(cursor.getColumnIndex(totalItems))
        val creationDate = cursor.getString(cursor.getColumnIndex(creationDate))
        val modificationDate = cursor.getString(cursor.getColumnIndex(modificationDate))

        return Category(name, color, totalItems, DateHelper.getStringAsDate(creationDate),
                DateHelper.getStringAsDate(modificationDate))
    }

    override fun toValues(category: Category): ContentValues {
        val values = ContentValues()
        values.put(name, category.name)
        values.put(color, category.color)
        values.put(totalItems, category.totalItems)
        values.put(creationDate, DateHelper.getDateAsString(category.creationDate))
        values.put(modificationDate, DateHelper.getDateAsString(category.modificationDate))

        return values
    }

}