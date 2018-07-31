package com.devcorerd.pos.view.main.category

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.Adapter
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.listener.OnCategoryDeleted
import com.devcorerd.pos.listener.OnCategoryUpdated
import com.devcorerd.pos.listener.OnProductAddedListener
import com.devcorerd.pos.model.entity.Category
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.model.presenter.CategoryPresenter
import com.devcorerd.pos.view.main.item.AddProductFragment
import com.devcorerd.pos.view.viewholder.CategoryProductViewHolder
import kotlinx.android.synthetic.main.category_details_fragment.*

/**
 * @author Ing. Wilson Garcia
 * Created on 7/30/18
 */
class CategoryDetailFragment : FragmentBase(), OnProductAddedListener, OnCategoryUpdated,
        OnCategoryDeleted {

    private var position: Int = 0

    private lateinit var selectedCategory: Category
    private lateinit var updateListener: OnCategoryUpdated
    private lateinit var deleteListener: OnCategoryDeleted

    private lateinit var productList: MutableList<Product>
    private val adapter: Adapter<Product, CategoryProductViewHolder> by lazy {
        Adapter(productList, productListRV.context, {
            val view: View = LayoutInflater.from(productListRV.context)
                    .inflate(R.layout.category_product_item, productListRV, false)
            CategoryProductViewHolder(view)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(category: Category, updateListener: OnCategoryUpdated,
                        deleteListener: OnCategoryDeleted, position: Int):
                CategoryDetailFragment {
            val fragmentBase = CategoryDetailFragment()
            val layout: Int = R.layout.category_details_fragment

            fragmentBase.selectedCategory = category
            fragmentBase.updateListener = updateListener
            fragmentBase.deleteListener = deleteListener
            fragmentBase.position = position
            fragmentBase.createBundle(layout, CategoryPresenter())
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEvents()

        toolbarTitle.text = selectedCategory.name
        searchCategory.hint = resources.getString(R.string.search_in)
                .plus(" ").plus(selectedCategory.name)

        (presenter as CategoryPresenter).getProductsByCategory(selectedCategory.name, { products: MutableList<Product>? ->

            productList = products!!

            productListRV.setHasFixedSize(false)
            productListRV.layoutManager = LinearLayoutManager(context!!)
            productListRV.adapter = adapter


        }, { error: Throwable ->
            error.printStackTrace()
        })
    }

    private fun setupEvents(){
        backButton.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().remove(this).commit()
        }

        createItemButton.setOnClickListener {
            stackFragmentToTop(AddProductFragment.newInstance(this, selectedCategory),
                    R.id.mainContainer, false)
        }

        editCategoryButton.setOnClickListener {
            stackFragmentToTop(UpdateCategoryFragment.newInstance(this,
                    this, position, selectedCategory),
                    R.id.mainContainer, false)
        }
    }

    override fun onProductAdded(product: Product) {
        if(product.category == selectedCategory.name)
            adapter.add(product)
    }

    override fun onCategoryDeleted(position: Int) {
        deleteListener.onCategoryDeleted(position)
    }

    override fun onCategoryUpdated(position: Int, category: Category) {
        updateListener.onCategoryUpdated(position, category)
    }

}