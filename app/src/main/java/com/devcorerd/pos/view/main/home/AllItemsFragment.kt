package com.devcorerd.pos.view.main.home

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.Adapter
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.model.presenter.ProductPresenter
import com.devcorerd.pos.view.viewholder.ProductListViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.all_items_fragment.*
import org.joda.time.DateTime

/**
 * @author Ing. Wilson Garcia
 * Created on 7/17/18
 */
class AllItemsFragment : FragmentBase() {

    private lateinit var productList: MutableList<Product>
    private val adapter: Adapter<Product, ProductListViewHolder> by lazy {
        Adapter(productList, productListRV.context, {
            val view: View = LayoutInflater.from(productListRV.context)
                    .inflate(R.layout.product_list_item, productListRV, false)
            ProductListViewHolder(view)
        }, object:  OnClickListener<Product>{
            override fun onClick(entity: Product?, `object`: Any?) {
                (activity!! as HomeActivity).makeFlyAnimation(`object` as CircleImageView,
                        entity?.productQuantity)
                (activity!! as HomeActivity).updateCharge(entity!!)
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance():
                AllItemsFragment {
            val fragmentBase = AllItemsFragment()
            val layout: Int = R.layout.all_items_fragment

            fragmentBase.createBundle(layout, ProductPresenter())
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (presenter as ProductPresenter).getProducts({products: MutableList<Product> ->
            productList = products

            productListRV.setHasFixedSize(false)
            productListRV.layoutManager = LinearLayoutManager(context!!)
            productListRV.adapter = adapter
        }, {error: Throwable ->
            print(error.message)
        })

    }
}