package com.devcorerd.pos.model.entity

import org.joda.time.DateTime

/**
 * Created by wgarcia on 7/26/2018.
 */
data class Category(var name: String, var color: String, var creationDate: DateTime,
                    var modificationDate: DateTime){
    val products: MutableList<Product> = mutableListOf()
}