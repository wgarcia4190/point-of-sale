package com.devcorerd.pos.view.viewholder

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.ViewHolder
import com.devcorerd.pos.helper.Helper
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.model.presenter.ProductPresenter
import com.devcorerd.pos.view.custom.CircleImageView

/**
 * @author Ing. Wilson Garcia
 * Created on 7/25/18
 */
class ProductItemViewHolder(view: View, val presenter: ProductPresenter) : ViewHolder<Product>(view) {

    private val productImage: CircleImageView by lazy {
        view.findViewById<CircleImageView>(R.id.productImage)
    }

    private val productContainer: LinearLayout by lazy {
        view.findViewById<LinearLayout>(R.id.productContainer)
    }

    private val favoriteButton: ImageButton by lazy {
        view.findViewById<ImageButton>(R.id.favoriteButton)
    }

    private val productName: TextView by lazy { view.findViewById<TextView>(R.id.productName) }
    private val productPrice: TextView by lazy { view.findViewById<TextView>(R.id.productPrice) }

    override fun bindElement(entity: Product, context: Context, listener: OnClickListener<Product>?) {
        productName.text = entity.name
        productPrice.text = "$".plus(String.format("%.2f", entity.price))

        if (!entity.hasImage)
            productImage.setColor(entity.categoryColor)
        else
            productImage.setImageBitmap(Helper.getBitmapFromString(entity.representation))

        if(entity.isFavorite)
            favoriteButton.setColorFilter(ContextCompat.getColor(context, R.color.favorite),
                    android.graphics.PorterDuff.Mode.SRC_IN)

        productContainer.setOnClickListener {
            listener?.onClick(entity, layoutPosition)
        }

        favoriteButton.setOnClickListener {
            entity.isFavorite = !entity.isFavorite
            presenter.updateProduct(entity, {
                if(entity.isFavorite)
                    favoriteButton.setColorFilter(ContextCompat.getColor(context, R.color.favorite),
                        android.graphics.PorterDuff.Mode.SRC_IN)
                else
                    favoriteButton.setColorFilter(ContextCompat.getColor(context, R.color.black),
                            android.graphics.PorterDuff.Mode.SRC_IN)

            }, { error: Throwable ->
                UIHelper.showMessage(context!!, "Error actualizando producto", error.message!!)
                error.printStackTrace()
            })
        }

    }

}