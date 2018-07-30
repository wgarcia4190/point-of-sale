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
import com.devcorerd.pos.helper.UIHelper


@Suppress("DEPRECATION")
/**
 * Created by wgarcia on 7/27/2018.
 */
class CategorySelectorViewHolder(view: View, var selectedCategory: Category?):
        ViewHolder<Category>(view){

    private val separator: View by lazy { view.findViewById<View>(R.id.separator) }
    private val categoryName: TextView by lazy { view.findViewById<TextView>(R.id.categoryName) }
    private val categoryRadioButton: AppCompatRadioButton by lazy {
        view.findViewById<AppCompatRadioButton>(R.id.categoryRadioButton)
    }
    @SuppressLint("RestrictedApi")
    override fun bindElement(entity: Category, context: Context,
                             listener: OnClickListener<Category>?) {
        categoryName.text = entity.name

        categoryRadioButton.setOnCheckedChangeListener { _, _ ->
            if(UIHelper.selectedCategoryRadio != null) UIHelper.selectedCategoryRadio!!.isChecked = false
            if(UIHelper.selectedCategoryTextView != null) UIHelper.selectedCategoryTextView!!
                    .setTextColor(context.resources.getColor(R.color.black))

            setupComponents(context, listener, entity)
        }

        if(layoutPosition > 0)
            separator.visibility = View.GONE

        if(selectedCategory == null && layoutPosition == 0){
            categoryRadioButton.isChecked = true
            setupComponents(context, listener, entity)
        }

    }

    private fun setupComponents(context: Context, listener: OnClickListener<Category>?, entity: Category) {
        UIHelper.selectedCategoryRadio = categoryRadioButton
        UIHelper.selectedCategoryTextView = categoryName

        categoryName.setTextColor(context.resources.getColor(R.color.colorPrimary))
        listener?.onClick(entity, null)
    }

}