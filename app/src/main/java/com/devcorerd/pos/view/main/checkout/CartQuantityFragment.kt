package com.devcorerd.pos.view.main.checkout

import android.os.Bundle
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.listener.OnCartQuantityUpdated
import com.devcorerd.pos.model.entity.CartItem
import kotlinx.android.synthetic.main.cart_quantity_fragment.*

/**
 * @author Ing. Wilson Garcia
 * Created on 7/31/18
 */
class CartQuantityFragment : FragmentBase() {

    private lateinit var cartItem: CartItem
    private lateinit var listener: OnCartQuantityUpdated

    companion object {
        @JvmStatic
        fun newInstance(cartItem: CartItem, listener: OnCartQuantityUpdated):
                CartQuantityFragment {
            val fragmentBase = CartQuantityFragment()
            val layout: Int = R.layout.cart_quantity_fragment

            fragmentBase.cartItem = cartItem
            fragmentBase.listener = listener
            fragmentBase.createBundle(layout)
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productName.text = cartItem.product.name
        quantitySelector.setQuantity(cartItem.quantity)

        setupEvents()
    }

    private fun setupEvents() {
        backButton.setOnClickListener {
            removeFragment()
        }

        updateCartButton.setOnClickListener {
            listener.onCartQuantityUpdated(cartItem, quantitySelector.getQuantity())
            backButton.performClick()
        }
    }

}