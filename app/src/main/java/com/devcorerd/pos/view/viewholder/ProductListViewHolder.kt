package com.devcorerd.pos.view.viewholder

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.ViewHolder
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.view.custom.CircleImageView
import com.devcorerd.pos.R.id.imageView
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ColorDrawable




/**
 * Created by wgarcia on 7/19/2018.
 */
class ProductListViewHolder(view: View) : ViewHolder<Product>(view) {

    private val productImage: CircleImageView by lazy {
        view.findViewById<CircleImageView>(R.id.productImage)
    }

    private val productContainer: LinearLayout by lazy {
        view.findViewById<LinearLayout>(R.id.productContainer)
    }

    private val productName: TextView by lazy { view.findViewById<TextView>(R.id.productName) }
    private val productPrice: TextView by lazy { view.findViewById<TextView>(R.id.productPrice) }

    override fun bindElement(entity: Product, context: Context, listener: OnClickListener<Product>?) {
        productName.text = entity.name
        productPrice.text = "$".plus(String.format("%.2f", entity.price))

        if (!entity.hasImage)
            productImage.setColor(entity.representation)

        productContainer.setOnClickListener {
            listener?.onClick(entity, productImage)
        }

    }


}