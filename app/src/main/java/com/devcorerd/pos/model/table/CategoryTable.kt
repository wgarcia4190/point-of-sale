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

        const val name = "name"
        const val color = "color"
        const val creationDate = "creationDate"
        const val modificationDate = "modificationDate"
    }


    override fun onCreate(database: SQLiteDatabase) {
        TableBuilder.create(this)
                .textColumn(name)
                .textColumn(color)
                .textColumn(creationDate)
                .textColumn(modificationDate)
                .execute(database)
    }

    override fun fromCursor(cursor: Cursor): Category {
        val name = cursor.getString(cursor.getColumnIndex(name))
        val color = cursor.getString(cursor.getColumnIndex(color))
        val creationDate = cursor.getString(cursor.getColumnIndex(creationDate))
        val modificationDate = cursor.getString(cursor.getColumnIndex(modificationDate))

        return Category(name, color, DateHelper.getStringAsDate(creationDate),
                DateHelper.getStringAsDate(modificationDate))
    }

    override fun toValues(category: Category): ContentValues {
        val values = ContentValues()
        values.put(name, category.name)
        values.put(color, category.color)
        values.put(creationDate, DateHelper.getDateAsString(category.creationDate))
        values.put(modificationDate, DateHelper.getDateAsString(category.modificationDate))

        return values
    }

}