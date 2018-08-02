package com.devcorerd.pos.listener

import com.devcorerd.pos.model.entity.Product

/**
 * Created by wgarcia on 7/31/2018.
 */
interface OnProductUpdated {
    fun onProductUpdated(position: Int, product: Product)
}