package com.devcorerd.pos.view.viewholder

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.ViewHolder
import com.devcorerd.pos.helper.Helper
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.view.custom.CircleImageView
import com.devcorerd.pos.view.custom.QuantitySelector


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

    private val quantitySelector: QuantitySelector by lazy {
        view.findViewById<QuantitySelector>(R.id.productQuantity)
    }

    private val productName: TextView by lazy { view.findViewById<TextView>(R.id.productName) }
    private val productPrice: TextView by lazy { view.findViewById<TextView>(R.id.productPrice) }

    override fun bindElement(entity: Product, context: Context, listener: OnClickListener<Product>?) {
        productName.text = entity.name
        productPrice.text = "$".plus(String.format("%.2f", entity.price))

        if (!entity.hasImage)
            productImage.setColor(entity.representation)
        else
            productImage.setImageBitmap(Helper.getBitmapFromString(entity.representation))

        productContainer.setOnClickListener {
            entity.setQuantity(quantitySelector.getQuantity())
            listener?.onClick(entity, productImage)
        }

    }


}