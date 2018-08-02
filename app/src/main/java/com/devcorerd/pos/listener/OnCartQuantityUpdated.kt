package com.devcorerd.pos.listener

import com.devcorerd.pos.model.entity.CartItem

/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
interface OnCartQuantityUpdated {
    fun onCartQuantityUpdated(cartItem: CartItem, quantity: Int)
}