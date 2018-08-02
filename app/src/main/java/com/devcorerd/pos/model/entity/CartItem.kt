package com.devcorerd.pos.model.entity

/**
 * @author Ing. Wilson Garcia
 * Created on 7/31/18
 */
data class CartItem(val product: Product, var quantity: Int) {

    override fun toString(): String {
        return product.name
    }

    override fun equals(other: Any?): Boolean {
        return product.name.equals((other as CartItem).product.name)
    }
}