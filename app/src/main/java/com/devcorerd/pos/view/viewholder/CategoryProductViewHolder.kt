package com.devcorerd.pos.view.viewholder

import android.content.Context
import android.view.View
import android.widget.TextView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.ViewHolder
import com.devcorerd.pos.helper.Helper
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.view.custom.CircleImageView

/**
 * Created by wgarcia on 7/31/2018.
 */
class CategoryProductViewHolder(view: View) : ViewHolder<Product>(view) {

    private val productImage: CircleImageView by lazy {
        view.findViewById<CircleImageView>(R.id.productImage)
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
            productImage.setColor(entity.categoryColor)
        else
            productImage.setImageBitmap(Helper.getBitmapFromString(entity.representation))
    }

}