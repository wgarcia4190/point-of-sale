package com.devcorerd.pos.view.main.checkout

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.Adapter
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.listener.OnCartItemDeleted
import com.devcorerd.pos.listener.OnCartQuantityUpdated
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.CartItem
import com.devcorerd.pos.view.main.home.HomeActivity
import com.devcorerd.pos.view.viewholder.CartItemViewHolder
import kotlinx.android.synthetic.main.cart_fragment.*

/**
 * @author Ing. Wilson Garcia
 * Created on 7/31/18
 */
class CartFragment : FragmentBase(), OnCartQuantityUpdated {

    private lateinit var items: MutableList<CartItem>
    private lateinit var listener: OnCartItemDeleted
    private var price: Double = 0.0
    private val adapter: Adapter<CartItem, CartItemViewHolder> by lazy {
        Adapter(items, cartListRV.context, {
            val view: View = LayoutInflater.from(cartListRV.context)
                    .inflate(R.layout.cart_item, cartListRV, false)
            CartItemViewHolder(view)
        }, object : OnClickListener<CartItem> {
            override fun onClick(entity: CartItem?, `object`: Any?) {
                val deleteItem: Boolean = `object` as Boolean

                if (deleteItem) {
                    adapter.delete(items.indexOf(entity))
                    listener.onProductDeleted(entity!!)

                    setTotal()
                } else
                    stackFragmentToTop(CartQuantityFragment.newInstance(entity!!,
                            this@CartFragment), R.id.mainContainer,
                            false)
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance(items: MutableList<CartItem>, listener: OnCartItemDeleted):
                CartFragment {
            val fragmentBase = CartFragment()
            val layout: Int = R.layout.cart_fragment

            fragmentBase.items = items
            fragmentBase.listener = listener
            fragmentBase.createBundle(layout)
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartListRV.setHasFixedSize(false)
        cartListRV.layoutManager = LinearLayoutManager(context!!)
        cartListRV.adapter = adapter

        setTotal()
        setupEvents()
    }

    private fun setTotal() {
        price = 0.0
        for (cartItem in items) {
            price += cartItem.product.price * cartItem.quantity
        }

        chargeButton.text = resources.getString(R.string.charge_wildcard)
                .replace("{price}", String.format("%.2f", price))

        if (price == 0.0)
            backButton.performClick()
    }

    private fun setupEvents() {
        backButton.setOnClickListener {
            removeFragment()
        }

        clearCartButton.setOnClickListener {
            listener.onAllProductsDeleted()
            backButton.performClick()
        }

        chargeButton.setOnClickListener {
            stackFragmentToTop(CheckoutFragment.newInstance(price, items), R.id.mainContainer,
                    false)
        }
    }

    override fun onCartQuantityUpdated(cartItem: CartItem, quantity: Int) {
        val newQuantity = quantity - cartItem.quantity
        if (newQuantity <= 0) {
            adapter.delete(items.indexOf(cartItem))
            listener.onProductDeleted(cartItem)
        } else {
            val tempCartItem = items[items.indexOf(element = cartItem)]
            tempCartItem.quantity = quantity
            adapter.notifyDataSetChanged()

            (activity as HomeActivity).updateCount(newQuantity)
            (activity as HomeActivity).updateCharge(tempCartItem, newQuantity)
        }

        setTotal()
    }

}