package com.devcorerd.pos.listener

import com.devcorerd.pos.model.entity.Product

/**
 * @author Ing. Wilson Garcia
 * Created on 7/25/18
 */
interface OnProductAddedListener {
    fun onProductAdded(product: Product)
}