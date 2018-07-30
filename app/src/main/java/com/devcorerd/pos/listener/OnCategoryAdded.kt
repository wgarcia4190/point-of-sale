package com.devcorerd.pos.listener

import com.devcorerd.pos.model.entity.Category

/**
 * @author Ing. Wilson Garcia
 * Created on 7/28/18
 */
interface OnCategoryAdded {
    fun onCategoryAdded(category: Category)
}