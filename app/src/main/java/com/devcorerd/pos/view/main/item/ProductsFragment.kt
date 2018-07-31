package com.devcorerd.pos.view.main.item

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.Adapter
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.listener.OnProductAddedListener
import com.devcorerd.pos.listener.OnProductDeleted
import com.devcorerd.pos.listener.OnProductUpdated
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.model.presenter.ProductPresenter
import com.devcorerd.pos.view.viewholder.ProductItemViewHolder
import kotlinx.android.synthetic.main.products_fragment.*

/**
 * @author Ing. Wilson Garcia
 * Created on 7/24/18
 */
class ProductsFragment : FragmentBase(), OnProductAddedListener, OnProductUpdated, OnProductDeleted {

    private lateinit var productList: MutableList<Product>
    private val adapter: Adapter<Product, ProductItemViewHolder> by lazy {
        Adapter(productList, productListRV.context, {
            val view: View = LayoutInflater.from(productListRV.context)
                    .inflate(R.layout.product_item, productListRV, false)
            ProductItemViewHolder(view, presenter as ProductPresenter)
        }, object:  OnClickListener<Product> {
            override fun onClick(entity: Product?, `object`: Any?) {
                stackFragmentToTop(UpdateProductFragment.newInstance(
                        this@ProductsFragment, this@ProductsFragment,
                        entity!!, `object` as Int), R.id.mainContainer, false)
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance():
                ProductsFragment {
            val fragmentBase = ProductsFragment()
            val layout: Int = R.layout.products_fragment

            fragmentBase.createBundle(layout, ProductPresenter())
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (presenter as ProductPresenter).getProducts({ products: MutableList<Product> ->
            productList = products

            productListRV.setHasFixedSize(false)
            productListRV.layoutManager = LinearLayoutManager(context!!)
            productListRV.adapter = adapter
        }, {error: Throwable ->
            print(error.message)
        })

        setupEvents()
    }

    private fun setupEvents() {
        backButton.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().remove(this).commit()
        }

        addProductButton.setOnClickListener {
            stackFragmentToTop(AddProductFragment.newInstance(this), R.id.mainContainer, false)
        }
    }

    override fun onProductAdded(product: Product) {
        adapter.add(product)
    }

    override fun onProductDeleted(position: Int) {
        adapter.delete(position)
    }

    override fun onCategoryUpdated(position: Int, product: Product) {
        adapter.update(product, position)
    }
}