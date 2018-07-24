package com.devcorerd.pos.listener

import com.devcorerd.pos.model.entity.Customer

/**
 * @author Ing. Wilson Garcia
 * Created on 7/24/18
 */
interface OnCustomerAddedListener {
    fun onCustomerAdded(customer: Customer)
}