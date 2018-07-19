package com.devcorerd.pos.view.viewholder

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.widget.TextView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.ViewHolder
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.Product
import de.hdodenhof.circleimageview.CircleImageView


/**
 * Created by wgarcia on 7/19/2018.
 */
class ProductListViewHolder(view: View): ViewHolder<Product>(view){

    private val productImage: CircleImageView by lazy {
        view.findViewById<CircleImageView>(R.id.productImage) }

    private val productName: TextView by lazy { view.findViewById<TextView>(R.id.productName) }
    private val productPrice: TextView by lazy { view.findViewById<TextView>(R.id.productPrice) }

    override fun bindElement(entity: Product, context: Context, listener: OnClickListener<Product>?) {
        productName.text = entity.name
        productPrice.text = "$".plus(String.format("%.2f", entity.price))

        if(!entity.hasImage)
            productImage.setBackgroundColor(Color.parseColor(entity.representation))

    }


}