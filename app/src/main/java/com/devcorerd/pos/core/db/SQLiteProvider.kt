package com.devcorerd.pos.core.db

import com.devcorerd.pos.model.table.*
import ru.arturvasilov.sqlite.core.SQLiteConfig
import ru.arturvasilov.sqlite.core.SQLiteContentProvider
import ru.arturvasilov.sqlite.core.SQLiteSchema

/**
 * @author Ing. Wilson Garcia
 * Created on 7/23/18
 */
class SQLiteProvider: SQLiteContentProvider() {

    private val databaseName = "rdpos.db"
    private val contentAuthority = "com.devcorerd.pos"

    override fun prepareSchema(schema: SQLiteSchema) {
        schema.register(ProductTable.TABLE)
        schema.register(CustomerTable.TABLE)
        schema.register(CategoryTable.TABLE)
        schema.register(PrinterTable.TABLE)
        schema.register(TransactionTable.TABLE)
        schema.register(TransactionDetailsTable.TABLE)
    }

    override fun prepareConfig(config: SQLiteConfig) {
        config.setDatabaseName(databaseName)
        config.setAuthority(contentAuthority)
    }
}