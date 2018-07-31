package com.devcorerd.pos.view.viewholder

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.ViewHolder
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.Category
import com.devcorerd.pos.view.custom.CircleImageView

/**
 * Created by wgarcia on 7/30/2018.
 */
class CategoryViewHolder(view: View): ViewHolder<Category>(view){

    private val categoryContainer: LinearLayout by lazy {
        view.findViewById<LinearLayout>(R.id.categoryContainer)
    }

    private val categoryName: TextView by lazy {
        view.findViewById<TextView>(R.id.categoryName)
    }

    private val categoryColor: CircleImageView by lazy {
        view.findViewById<CircleImageView>(R.id.categoryColor)
    }

    private val totalProducts: TextView by lazy {
        view.findViewById<TextView>(R.id.totalProducts)
    }

    override fun bindElement(entity: Category, context: Context, listener: OnClickListener<Category>?) {
        categoryName.text = entity.name
        categoryColor.setColor(entity.color)

        if(entity.totalItems == 1)
            totalProducts.text = entity.totalItems.toString().plus(" ")
                    .plus(context.resources.getString(R.string.product))
        else
            totalProducts.text = entity.totalItems.toString().plus(" ")
                    .plus(context.resources.getString(R.string.products))

        categoryContainer.setOnClickListener {
            listener?.onClick(entity, null)
        }

    }

}