package com.devcorerd.pos.view.viewholder

import android.content.Context
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.ViewHolder
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.CartItem

/**
 * @author Ing. Wilson Garcia
 * Created on 7/31/18
 */
class CartItemViewHolder(view: View) : ViewHolder<CartItem>(view) {

    private val quantity: TextView by lazy {
        view.findViewById<TextView>(R.id.quantity)
    }

    private val productName: TextView by lazy {
        view.findViewById<TextView>(R.id.productName)
    }

    private val productPrice: TextView by lazy {
        view.findViewById<TextView>(R.id.productPrice)
    }

    private val productPriceTotal: TextView by lazy {
        view.findViewById<TextView>(R.id.productPriceTotal)
    }

    private val deleteItem: ImageButton by lazy {
        view.findViewById<ImageButton>(R.id.deleteItem)
    }

    private val cartContainer: LinearLayout by lazy {
        view.findViewById<LinearLayout>(R.id.cartContainer)
    }

    override fun bindElement(entity: CartItem, context: Context, listener: OnClickListener<CartItem>?) {
        quantity.text = entity.quantity.toString()
        productName.text = entity.product.name
        productPrice.text = "$".plus(String.format("%.2f", entity.product.price))

        productPriceTotal.text = "$".plus(String.format("%.2f",
                (entity.product.price * entity.quantity)))

        deleteItem.setOnClickListener {
            listener?.onClick(entity, true)
        }

        cartContainer.setOnClickListener {
            listener?.onClick(entity, false)
        }
    }

}