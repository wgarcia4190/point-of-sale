package com.devcorerd.pos.model.entity

import org.joda.time.DateTime

/**
 * Created by wgarcia on 7/19/2018.
 */
data class Product(val name: String, val description: String, val category: String,
                   val soldBy: Char, val price: Double, val sku: String, val barcode: String?,
                   val representation: String, val hasImage: Boolean, val isFavorite: Boolean,
                   val creationDate: DateTime, val modificationDate: DateTime,
                   var productQuantity: Int = 1) {

    fun setQuantity(quantity: Int) {
        this.productQuantity = quantity
    }
}