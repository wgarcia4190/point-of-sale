package com.devcorerd.pos.listener

import com.devcorerd.pos.model.entity.CartItem

/**
 * @author Ing. Wilson Garcia
 * Created on 7/31/18
 */
interface OnCartItemDeleted{
    fun onAllProductsDeleted()
    fun onProductDeleted(cartItem: CartItem)
}