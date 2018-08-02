package com.devcorerd.pos.helper

import com.devcorerd.pos.model.entity.Category
import com.devcorerd.pos.model.entity.Customer

/**
 * Created by wgarcia on 7/16/2018.
 */
class ConstantsHelper private constructor(){
    companion object {
        const val layoutKey: String = "LayoutKey"
        const val applicationJson = "application/json"
        const val writeReadCode: Int = 7901
        const val cameraCode: Int = 8901
        const val scanCode: Int = 9000
        const val cardScanCode: Int = 8000
        const val defaultCategoryName = "Sin Asignar"
        const val defaultCategoryColor = "#7a7a7a"

        val categoryList: MutableList<Category> = mutableListOf()
        var defaultCategory: Category? = null
        var selectedCustomer: Customer? = null

    }
}