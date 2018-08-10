package com.devcorerd.pos.view.viewholder

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.ViewHolder
import com.devcorerd.pos.helper.Helper
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.view.custom.QuantitySelector

/**
 * Created by wgarcia on 8/10/2018.
 */
class ProductCardViewHolder(view: View) : ViewHolder<Product>(view) {

    private val productImage: ImageView by lazy {
        view.findViewById<ImageView>(R.id.productImage)
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
            productImage.setImageDrawable(ColorDrawable(Color.parseColor(entity.categoryColor)))
        else
            productImage.setImageBitmap(Helper.getBitmapFromString(entity.representation))

        productContainer.setOnClickListener {
            entity.setQuantity(quantitySelector.getQuantity())
            listener?.onClick(entity, productImage)
        }

    }

}