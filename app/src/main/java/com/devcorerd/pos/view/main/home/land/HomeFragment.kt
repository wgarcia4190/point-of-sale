package com.devcorerd.pos.view.main.home.land

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.Adapter
import com.devcorerd.pos.core.ui.ProductSearchFragment
import com.devcorerd.pos.helper.ConstantsHelper
import com.devcorerd.pos.helper.Helper
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.listener.OnCustomerSelected
import com.devcorerd.pos.listener.OnToggleDrawer
import com.devcorerd.pos.listener.OnUpdateToolbarListener
import com.devcorerd.pos.model.entity.Customer
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.model.presenter.ProductPresenter
import com.devcorerd.pos.view.main.home.HomeActivity
import com.devcorerd.pos.view.viewholder.ProductListViewHolder
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.customer_header.*
import kotlinx.android.synthetic.main.home_fragment.*

@Suppress("PLUGIN_WARNING", "DEPRECATION", "UNCHECKED_CAST")
/**
 * Created by wgarcia on 8/7/2018.
 */
class HomeFragment : ProductSearchFragment(), OnCustomerSelected {

    private lateinit var listener: OnUpdateToolbarListener
    private lateinit var drawerListener: OnToggleDrawer

    private lateinit var productList: MutableList<Product>
    private val adapter: Adapter<Product, ProductListViewHolder> by lazy {
        Adapter(productList, productListRV.context, {
            val view: View = LayoutInflater.from(productListRV.context)
                    .inflate(R.layout.product_list_item, productListRV, false)
            ProductListViewHolder(view)
        }, object : OnClickListener<Product> {
            override fun onClick(entity: Product?, `object`: Any?) {
                (activity!! as HomeActivity).makeFlyAnimation(`object` as CircleImageView,
                        entity?.productQuantity)
                (activity!! as HomeActivity).updateCharge(entity!!)
            }

        })
    }

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
    }

    private fun setupDataCalls(){
        allItemsTab.tag = {
            (presenter as ProductPresenter).getProducts({ products: MutableList<Product> ->
                if(products.isEmpty() && Helper.isInternetAvailable(context!!)) {
                    (presenter as ProductPresenter).getProductsFromServer({
                        fillList(it)
                    }, { error: Throwable ->
                        UIHelper.showMessage(context!!, "Error cargando Productos", error.message!!)
                    })
                }else
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

    private fun toggleTabs(selectedView: View, deselectedView: View){
        selectedView.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
        deselectedView.setBackgroundColor(resources.getColor(R.color.colorPrimary))


        (selectedView.tag as ()->Unit).invoke()
    }

    private fun fillList(products: MutableList<Product>) {
        productList = products
        toggleList(productList, productListRV)

        productListRV.setHasFixedSize(false)
        productListRV.layoutManager = LinearLayoutManager(context)
        productListRV.adapter = adapter

        setupEvents(productList, adapter)
    }


    override fun onCustomerSelected(customer: Customer) {
        customerHeader.visibility = View.VISIBLE
        customerName.text = customer.getFullName()
        customerSocialID.text = customer.socialID
        customerEmail.text = customer.email
    }

}