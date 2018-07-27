package com.devcorerd.pos.view.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.AppCompatRadioButton
import android.view.View
import android.widget.TextView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.ViewHolder
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.Category
import android.content.res.ColorStateList



@Suppress("DEPRECATION")
/**
 * Created by wgarcia on 7/27/2018.
 */
class CategorySelectorViewHolder(view: View, var selectedCategoryRadio: AppCompatRadioButton?,
                                 var selectedCategoryTextView: TextView?): ViewHolder<Category>(view){

    private val separator: View by lazy { view.findViewById<View>(R.id.separator) }
    private val categoryName: TextView by lazy { view.findViewById<TextView>(R.id.categoryName) }
    private val categoryRadioButton: AppCompatRadioButton by lazy {
        view.findViewById<AppCompatRadioButton>(R.id.categoryRadioButton)
    }
    @SuppressLint("RestrictedApi")
    override fun bindElement(entity: Category, context: Context,
                             listener: OnClickListener<Category>?) {
        categoryName.text = entity.name

        val colorStateList = ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_checked)),
                intArrayOf(context.resources.getColor(R.color.colorPrimary))
        )

        if(layoutPosition > 0)
            separator.visibility = View.GONE

        categoryRadioButton.supportButtonTintList = colorStateList
        categoryRadioButton.setOnCheckedChangeListener { _, _ ->
            if(selectedCategoryRadio != null) selectedCategoryRadio!!.isChecked = false
            if(selectedCategoryTextView != null) selectedCategoryTextView!!
                    .setTextColor(context.resources.getColor(R.color.black))

            selectedCategoryRadio = categoryRadioButton
            selectedCategoryTextView = categoryName

            categoryName.setTextColor(context.resources.getColor(R.color.colorPrimary))

            selectedCategoryRadio!!.tag = entity
        }

    }

}