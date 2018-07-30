package com.devcorerd.pos.view.viewholder

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.ViewHolder
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.Category

/**
 * Created by wgarcia on 7/30/2018.
 */
class CategoryViewHolder(view: View, private val totalItems: Int): ViewHolder<Category>(view){

    private val categoryContainer: LinearLayout by lazy {
        view.findViewById<LinearLayout>(R.id.categoryContainer)
    }

    private val categoryName: TextView by lazy {
        view.findViewById<TextView>(R.id.categoryName)
    }

    private val totalProducts: TextView by lazy {
        view.findViewById<TextView>(R.id.totalProducts)
    }

    override fun bindElement(entity: Category, context: Context, listener: OnClickListener<Category>?) {
        categoryName.text = entity.name
        totalProducts.text = totalItems.toString()
    }

}