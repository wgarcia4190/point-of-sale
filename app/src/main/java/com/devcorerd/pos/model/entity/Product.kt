package com.devcorerd.pos.model.entity

import org.joda.time.DateTime

/**
 * Created by wgarcia on 7/19/2018.
 */
data class Product(var name: String, var description: String, var category: String,
                   var categoryColor: String, var soldBy: Char, var price: Double, val sku: String,
                   val barcode: String?, var representation: String, val hasImage: Boolean,
                   var isFavorite: Boolean, val creationDate: DateTime,
                   var modificationDate: DateTime, var productQuantity: Int = 1) {

    fun setQuantity(quantity: Int) {
        this.productQuantity = quantity
    }
}