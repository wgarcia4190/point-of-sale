package com.devcorerd.pos.model.entity

/**
 * Created by wgarcia on 7/23/2018.
 */
data class Customer(val name: String, val lastName: String, val socialID: String,
                    val email: String, val card: String) {

    fun getFullName(): String {
        return this.name.plus(" ").plus(this.lastName)
    }
}