package com.devcorerd.pos.view.main.home.land

import android.animation.Animator
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.Adapter
import com.devcorerd.pos.core.ui.ProductSearchFragment
import com.devcorerd.pos.helper.CircleAnimationHelper
import com.devcorerd.pos.helper.ConstantsHelper
import com.devcorerd.pos.helper.Helper
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.listener.OnCustomerSelected
import com.devcorerd.pos.listener.OnToggleDrawer
import com.devcorerd.pos.listener.OnUpdateToolbarListener
import com.devcorerd.pos.model.entity.CartItem
import com.devcorerd.pos.model.entity.Customer
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.model.presenter.ProductPresenter
import com.devcorerd.pos.view.viewholder.CartItemViewHolder
import com.devcorerd.pos.view.viewholder.ProductCardViewHolder
import kotlinx.android.synthetic.main.customer_land_header.*
import kotlinx.android.synthetic.main.home_fragment.*

@Suppress("PLUGIN_WARNING", "DEPRECATION", "UNCHECKED_CAST")
/**
 * Created by wgarcia on 8/7/2018.
 */
class HomeFragment : ProductSearchFragment(), OnCustomerSelected {

    private lateinit var listener: OnUpdateToolbarListener
    private lateinit var drawerListener: OnToggleDrawer

    private lateinit var productList: MutableList<Product>
    private val adapter: Adapter<Product, ProductCardViewHolder> by lazy {
        Adapter(productList, productListRV.context, {
            val view: View = LayoutInflater.from(productListRV.context)
                    .inflate(R.layout.product_item_card, productListRV, false)
            ProductCardViewHolder(view)
        }, object : OnClickListener<Product> {
            override fun onClick(entity: Product?, `object`: Any?) {
                makeFlyAnimation(`object` as ImageView, entity!!)
            }

        })
    }

    private var products: MutableList<CartItem> = mutableListOf()
    private val cartAdapter: Adapter<CartItem, CartItemViewHolder> by lazy {
        Adapter(products, cartListRV.context, {
            val view: View = LayoutInflater.from(cartListRV.context)
                    .inflate(R.layout.cart_item, cartListRV, false)
            CartItemViewHolder(view)
        }, object : OnClickListener<CartItem> {
            override fun onClick(entity: CartItem?, `object`: Any?) {

            }

        })
    }

    private var cartTotal: Double = 0.0

    companion object {
        @JvmStatic
        fun newInstance(listener: OnUpdateToolbarListener, drawerListener: OnToggleDrawer):
                HomeFragment {
            val fragmentBase = HomeFragment()
            val layout: Int = R.layout.home_fragment

            fragmentBase.listener = listener
            fragmentBase.drawerListener = drawerListener
            fragmentBase.createBundle(layout, ProductPresenter())
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEvents()
        listener.onUpdateToolbar(null)
        onCustomerSelected(ConstantsHelper.selectedCustomer!!)

        setupDataCalls()
        toggleTabs(allItemsTab, favoriteItemsTab)

        cartListRV.setHasFixedSize(false)
        cartListRV.layoutManager = LinearLayoutManager(context)
        cartListRV.adapter = cartAdapter
    }

    private fun setupDataCalls() {
        allItemsTab.tag = {
            (presenter as ProductPresenter).getProducts({ products: MutableList<Product> ->
                if (products.isEmpty() && Helper.isInternetAvailable(context!!)) {
                    (presenter as ProductPresenter).getProductsFromServer({
                        fillList(it)
                    }, { error: Throwable ->
                        UIHelper.showMessage(context!!, "Error cargando Productos", error.message!!)
                    })
                } else
                    fillList(products)


            }, { error: Throwable ->
                UIHelper.showMessage(context!!, "Error cargando Productos", error.message!!)
            })
        }

        favoriteItemsTab.tag = {
            (presenter as ProductPresenter).getFavoriteProducts({ products: MutableList<Product> ->
                productList = products
                adapter.swap(productList)
            }, { error: Throwable ->
                print(error.message)
            })

            setupEvents(productList, adapter)
        }
    }

    private fun setupEvents() {
        drawerButton.setOnClickListener {
            drawerListener.onToggleDrawer()
        }

        allItemsTab.setOnClickListener {
            toggleTabs(allItemsTab, favoriteItemsTab)
        }

        favoriteItemsTab.setOnClickListener {
            toggleTabs(favoriteItemsTab, allItemsTab)
        }
    }

    private fun toggleTabs(selectedView: View, deselectedView: View) {
        selectedView.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
        deselectedView.setBackgroundColor(resources.getColor(R.color.colorPrimary))


        (selectedView.tag as () -> Unit).invoke()
    }

    private fun fillList(products: MutableList<Product>) {
        productList = products
        toggleList(productList, productListRV)

        productListRV.setHasFixedSize(false)
        productListRV.layoutManager = GridLayoutManager(context, 3)
        productListRV.adapter = adapter

        setupEvents(productList, adapter)
    }

    private fun makeFlyAnimation(imageView: ImageView, product: Product?) {
        CircleAnimationHelper().attachActivity(activity!!).setTargetView(imageView)
                .setCircleDuration(200).setMoveDuration(400).setDestView(chargeButton)
                .setAnimationListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) {
                        updateCharge(product!!)
                    }

                    override fun onAnimationCancel(animation: Animator?) {}

                    override fun onAnimationStart(animation: Animator?) {}

                }).startAnimation()
    }

    private fun updateCharge(product: Product) {
        val cartItem = CartItem(product, product.productQuantity)

        if (!products.contains(cartItem))
            cartAdapter.add(cartItem)
        else {
            val index = products.indexOf(cartItem)
            cartItem.quantity = products[index].quantity + product.productQuantity
            cartAdapter.update(cartItem, index)
        }
        cartTotal += (product.price * product.productQuantity)
        updateCharge(cartTotal)
        product.productQuantity = 1
    }

    private fun updateCharge(price: Double) {
        val charge: String = resources.getString(R.string.charge_wildcard)
                .replace("{price}", String.format("%.2f", price))

        chargeButton.text = charge
        if (price > 0.0) {
            chargeButton.isEnabled = true
            chargeButton.setTextColor(resources.getColor(R.color.white))
        } else {
            chargeButton.isEnabled = false
            chargeButton.setTextColor(resources.getColor(R.color.deselected))
        }

    }


    override fun onCustomerSelected(customer: Customer) {
        customerLandHeader.visibility = View.VISIBLE
        customerName.text = customer.getFullName()
        customerSocialID.text = customer.socialID
        customerEmail.text = customer.email
    }

}