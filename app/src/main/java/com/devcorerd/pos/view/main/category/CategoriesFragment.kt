package com.devcorerd.pos.view.main.category

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.Adapter
import com.devcorerd.pos.core.ui.FragmentBase
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
                stackFragmentToTop(CategoryDetailFragment.newInstance(entity!!,
                        this@CategoriesFragment,
                        this@CategoriesFragment, `object` as Int),
                        R.id.mainContainer, false)
            }
        })
    }

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
            error.printStackTrace()
        })
    }

    private fun setupEvents() {
        backButton.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().remove(this).commit()
        }

        addCategoryButton.setOnClickListener {
            stackFragmentToTop(AddCategoryFragment.newInstance(this, true),
                    R.id.mainContainer, false)
        }
    }

    override fun onCategoryAdded(category: Category) {
        adapter.add(category)
        reloadData()
    }
    override fun onCategoryDeleted(position: Int) {
        adapter.delete(position)
    }

    override fun onCategoryUpdated(position: Int, category: Category) {
        adapter.update(category, position)
    }


}