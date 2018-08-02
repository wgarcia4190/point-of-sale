package com.devcorerd.pos.view.main.category

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.Adapter
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.TextWatcher
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.listener.*
import com.devcorerd.pos.model.entity.Category
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.model.presenter.CategoryPresenter
import com.devcorerd.pos.view.main.item.AddProductFragment
import com.devcorerd.pos.view.main.item.UpdateProductFragment
import com.devcorerd.pos.view.viewholder.CategoryProductViewHolder
import kotlinx.android.synthetic.main.category_details_fragment.*

/**
 * @author Ing. Wilson Garcia
 * Created on 7/30/18
 */
class CategoryDetailFragment : FragmentBase(), OnProductAddedListener, OnCategoryUpdated,
        OnCategoryDeleted, OnProductUpdated, OnProductDeleted {

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
        }, object : OnClickListener<Product> {
            override fun onClick(entity: Product?, `object`: Any?) {
                val index = if (!isFilter) `object` as Int else productList.indexOf(entity)

                stackFragmentToTop(UpdateProductFragment.newInstance(
                        this@CategoryDetailFragment, this@CategoryDetailFragment,
                        entity!!, index), R.id.mainContainer, false)
            }

        })
    }

    private var isFilter = false

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
        searchProduct.hint = resources.getString(R.string.search_in)
                .plus(" ").plus(selectedCategory.name)

        (presenter as CategoryPresenter).getProductsByCategory(selectedCategory.name, { products: MutableList<Product>? ->

            productList = products!!

            productListRV.setHasFixedSize(false)
            productListRV.layoutManager = LinearLayoutManager(context!!)
            productListRV.adapter = adapter


        }, { error: Throwable ->
            UIHelper.showMessage(context!!, "Error cargando productos", error.message!!)
            error.printStackTrace()
        })
    }

    private fun setupEvents() {
        backButton.setOnClickListener {
            removeFragment()
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

        searchProduct.addTextChangedListener(object : TextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)

                if (s!!.isNotEmpty()) {
                    clearSearch.visibility = View.VISIBLE
                    buttonsContainer.visibility = View.GONE
                    isFilter = true
                } else {
                    clearSearch.visibility = View.GONE
                    buttonsContainer.visibility = View.VISIBLE
                    isFilter = false
                }

                val filterList: MutableList<Product> = mutableListOf()
                for (product in productList) {
                    if (product.name.contains(s.toString(), true))
                        filterList.add(product)
                }

                adapter.swap(filterList)
            }
        })

        clearSearch.setOnClickListener {
            searchProduct.setText("")
            adapter.swap(productList)

            isFilter = false
        }
    }

    override fun onProductAdded(product: Product) {
        if (product.category == selectedCategory.name) {
            clearFilter()
            adapter.add(product)
        }
    }

    override fun onProductUpdated(position: Int, product: Product) {
        if (product.category == selectedCategory.name) {
            clearFilter()
            adapter.update(product, position)
        }
    }

    override fun onProductDeleted(position: Int) {
        clearFilter()
        adapter.delete(position)

    }


    override fun onCategoryDeleted(position: Int) {
        clearFilter()
        deleteListener.onCategoryDeleted(position)
    }

    override fun onCategoryUpdated(position: Int, category: Category) {
        clearFilter()
        updateListener.onCategoryUpdated(position, category)
    }

    private fun clearFilter() {
        adapter.swap(productList)
        clearSearch.visibility = View.GONE
        buttonsContainer.visibility = View.VISIBLE
        searchProduct.setText("")
        isFilter = false


    }

}