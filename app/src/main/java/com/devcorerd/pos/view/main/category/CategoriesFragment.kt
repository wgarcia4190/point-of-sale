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
import com.devcorerd.pos.listener.OnCategoryAdded
import com.devcorerd.pos.listener.OnCategoryDeleted
import com.devcorerd.pos.listener.OnCategoryUpdated
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.Category
import com.devcorerd.pos.model.presenter.CategoryPresenter
import com.devcorerd.pos.view.viewholder.CategoryViewHolder
import kotlinx.android.synthetic.main.categories_fragment.*

/**
 * @author Ing. Wilson Garcia
 * Created on 7/29/18
 */
class CategoriesFragment : FragmentBase(), OnCategoryAdded, OnCategoryUpdated, OnCategoryDeleted {

    private lateinit var categoryList: MutableList<Category>
    private val adapter: Adapter<Category, CategoryViewHolder> by lazy {
        Adapter(categoryList, categoryListRV.context, {
            val view: View = LayoutInflater.from(categoryListRV.context)
                    .inflate(R.layout.category_item, categoryListRV, false)
            CategoryViewHolder(view)
        }, object : OnClickListener<Category> {
            override fun onClick(entity: Category?, `object`: Any?) {
                val index = if (!isFilter) `object` as Int else categoryList.indexOf(entity)

                stackFragmentToTop(CategoryDetailFragment.newInstance(entity!!,
                        this@CategoriesFragment,
                        this@CategoriesFragment, index),
                        R.id.mainContainer, false)
            }
        })
    }

    private var isFilter = false

    companion object {
        @JvmStatic
        fun newInstance():
                CategoriesFragment {
            val fragmentBase = CategoriesFragment()
            val layout: Int = R.layout.categories_fragment

            fragmentBase.createBundle(layout, CategoryPresenter())
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEvents()
        reloadData()
    }

    private fun reloadData() {
        (presenter as CategoryPresenter).getCategories({ categories: MutableList<Category> ->

            categoryList = categories
            categoryListRV.setHasFixedSize(false)
            categoryListRV.layoutManager = LinearLayoutManager(context!!)
            categoryListRV.adapter = adapter

        }, { error: Throwable ->
            UIHelper.showMessage(context!!, "Error cargando categorias", error.message!!)
            error.printStackTrace()
        })
    }

    private fun setupEvents() {
        backButton.setOnClickListener {
            removeFragment()
        }

        addCategoryButton.setOnClickListener {
            stackFragmentToTop(AddCategoryFragment.newInstance(this, true),
                    R.id.mainContainer, false)
        }

        searchCategory.addTextChangedListener(object : TextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)

                if (s!!.isNotEmpty()) {
                    clearSearch.visibility = View.VISIBLE
                    isFilter = true
                }else {
                    clearSearch.visibility = View.GONE
                    isFilter = false
                }

                val filterList: MutableList<Category> = mutableListOf()
                for (category in categoryList) {
                    if (category.name.contains(s.toString(), true))
                        filterList.add(category)
                }

                adapter.swap(filterList)
            }
        })

        clearSearch.setOnClickListener {
            searchCategory.setText("")
            adapter.swap(categoryList)

            isFilter = false
        }
    }

    override fun onCategoryAdded(category: Category) {
        clearFilter()
        adapter.add(category)
        reloadData()
    }

    override fun onCategoryDeleted(position: Int) {
        clearFilter()
        adapter.delete(position)

    }

    override fun onCategoryUpdated(position: Int, category: Category) {
        clearFilter()
        adapter.update(category, position)
    }

    private fun clearFilter(){
        adapter.swap(categoryList)
        isFilter = false
        searchCategory.setText("")

    }


}