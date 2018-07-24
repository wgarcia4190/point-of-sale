package com.devcorerd.pos.listener

import com.devcorerd.pos.model.entity.Customer

/**
 * Created by wgarcia on 7/24/2018.
 */
interface OnCustomerSelected {

    fun onCustomerSelected(customer: Customer)
}