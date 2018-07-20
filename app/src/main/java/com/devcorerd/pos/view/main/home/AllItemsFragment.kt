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
                (activity!! as HomeActivity).makeFlyAnimation(`object` as CircleImageView)
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance():
                AllItemsFragment {
            val fragmentBase = AllItemsFragment()
            val layout: Int = R.layout.all_items_fragment

            fragmentBase.createBundle(layout)
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productList = mutableListOf(Product("Platano", "Platano Verde", "Viveres",
                'U', 10.00, "1000", "12345dd",
                "#FF0000", false, false, DateTime.now(),
                DateTime.now()),
                Product("Platano", "Platano Verde", "Viveres",
                        'U', 10.00, "1000", "12345dd",
                        "#FF0000", false, false, DateTime.now(),
                        DateTime.now()))

        productListRV.setHasFixedSize(false)
        productListRV.layoutManager = LinearLayoutManager(context!!)
        productListRV.adapter = adapter
    }
}