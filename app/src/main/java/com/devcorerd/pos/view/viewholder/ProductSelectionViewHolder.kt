package com.devcorerd.pos.view.viewholder

import android.content.Context
import android.support.v7.widget.AppCompatCheckBox
import android.view.View
import android.widget.TextView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.ViewHolder
import com.devcorerd.pos.helper.Helper
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.view.custom.CircleImageView

/**
 * @author Ing. Wilson Garcia
 * Created on 7/30/18
 */
class ProductSelectionViewHolder(view: View) : ViewHolder<Product>(view) {

    private val productImage: CircleImageView by lazy {
        view.findViewById<CircleImageView>(R.id.productImage)
    }

    private val productSelected: AppCompatCheckBox by lazy {
        view.findViewById<AppCompatCheckBox>(R.id.productSelected)
    }

    private val separator: View by lazy {
        view.findViewById<View>(R.id.separator)
    }

    private val productName: TextView by lazy { view.findViewById<TextView>(R.id.productName) }
    private val productCategory: TextView by lazy { view.findViewById<TextView>(R.id.productCategory) }


    override fun bindElement(entity: Product, context: Context, listener: OnClickListener<Product>?) {
        productName.text = entity.name
        productCategory.text = entity.category

        separator.visibility = if (layoutPosition == 0) View.VISIBLE else View.GONE

        if (!entity.hasImage)
            productImage.setColor(entity.representation)
        else
            productImage.setImageBitmap(Helper.getBitmapFromString(entity.representation))


        productSelected.setOnCheckedChangeListener { _, isChecked ->
            listener?.onClick(entity, isChecked)
        }
    }

}