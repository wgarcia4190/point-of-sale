package com.devcorerd.pos.listener

import com.devcorerd.pos.model.entity.Category

/**
 * Created by wgarcia on 7/31/2018.
 */
interface OnCategoryUpdated {
    fun onCategoryUpdated(position: Int, category: Category)
}