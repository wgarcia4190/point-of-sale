package com.devcorerd.pos.listener

import com.devcorerd.pos.model.entity.Category

/**
 * Created by wgarcia on 7/26/2018.
 */
interface OnCategorySelected {
    fun onCategorySelected(category: Category)
}