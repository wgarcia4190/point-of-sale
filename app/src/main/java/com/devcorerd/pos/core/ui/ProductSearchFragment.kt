package com.devcorerd.pos.core.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.Adapter
import com.devcorerd.pos.core.adapter.SpinnerAdapter
import com.devcorerd.pos.helper.ConstantsHelper
import com.devcorerd.pos.helper.TextWatcher
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.model.entity.Category
import com.devcorerd.pos.model.entity.Product
import kotlinx.android.synthetic.main.all_items_fragment.*
import kotlinx.android.synthetic.main.dropdown.*
import kotlinx.android.synthetic.main.product_search.*

/**
 * @author Ing. Wilson Garcia
 * Created on 7/31/18
 */
open class ProductSearchFragment: FragmentBase(){

    protected var isFilter = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryAdapter: SpinnerAdapter<Category> = SpinnerAdapter(context,
                R.layout.category_dropdown_view,
                ConstantsHelper.categoryList) { position: Int, convertView: View?,
                                                data: MutableList<Category>,
                                                callback: ((data: Any?) -> Unit)? ->

            val category: Category = data[position]
            val categoryColor: View = convertView!!.findViewById(R.id.categoryColor)
            val categoryName: TextView = convertView.findViewById(R.id.categoryName)
            val categoryContainer: LinearLayout = convertView.findViewById(R.id.categoryContainer)

            categoryColor.setBackgroundColor(Color.parseColor(category.color))
            categoryName.text = category.name

            categoryContainer.setOnClickListener {
                callback?.invoke(category)
            }

            return@SpinnerAdapter convertView
        }

        categoryAdapter.setDropDownViewResource(R.layout.category_dropdown_view)
        dropdownSpinner.adapter = categoryAdapter
    }

    protected open fun setupEvents(productList: MutableList<Product>, adapter: Adapter<Product, *>) {
        UIHelper.setSpinnerEvent(dropdownSpinner, dropdownContainer) {
            val category: Category = it as Category
            dropdownText.text = category.name
            clearFilter.visibility = View.VISIBLE

            val filterList: MutableList<Product> = mutableListOf()
            for (product in productList) {
                if (product.category == category.name)
                    filterList.add(product)
            }
            isFilter = true
            adapter.swap(filterList)
        }

        clearFilter.setOnClickListener {
            dropdownText.text = context!!.resources.getString(R.string.all_categories)
            adapter.swap(productList)

            clearFilter.visibility = View.GONE
            isFilter = false
        }

        searchProduct.setOnClickListener {
            categoryDropdownContainer.visibility = View.GONE
            productSearchContainer.visibility = View.VISIBLE
        }

        searchProductText.addTextChangedListener(object : TextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                val filterList: MutableList<Product> = mutableListOf()
                for (product in productList) {
                    if (product.name.contains(s.toString(), true))
                        filterList.add(product)
                }

                isFilter = true
                adapter.swap(filterList)
            }
        })

        clearSearch.setOnClickListener {
            searchProductText.setText("")
            adapter.swap(productList)

            categoryDropdownContainer.visibility = View.VISIBLE
            productSearchContainer.visibility = View.GONE

            isFilter = false
        }
    }

    protected fun clearFilters(productList: MutableList<Product>, adapter: Adapter<Product, *>){
        isFilter = false
        searchProductText.setText("")
        dropdownText.text = context!!.resources.getString(R.string.all_categories)
        adapter.swap(productList)

        clearFilter.visibility = View.GONE
        categoryDropdownContainer.visibility = View.VISIBLE
        productSearchContainer.visibility = View.GONE

    }

}