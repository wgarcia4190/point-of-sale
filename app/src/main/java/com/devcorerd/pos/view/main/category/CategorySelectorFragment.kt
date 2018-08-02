package com.devcorerd.pos.view.main.category

import android.os.Bundle
import android.support.v7.widget.AppCompatRadioButton
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.Adapter
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.listener.OnCategoryAdded
import com.devcorerd.pos.listener.OnCategorySelected
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.Category
import com.devcorerd.pos.model.presenter.CategoryPresenter
import com.devcorerd.pos.view.viewholder.CategorySelectorViewHolder
import kotlinx.android.synthetic.main.category_selector_fragment.*

/**
 * Created by wgarcia on 7/26/2018.
 */
class CategorySelectorFragment : FragmentBase(), OnCategoryAdded {

    private var selectedCategory: Category? = null
    private lateinit var listener: OnCategorySelected

    private lateinit var categoryList: MutableList<Category>
    private val adapter: Adapter<Category, CategorySelectorViewHolder> by lazy {
        Adapter(categoryList, categoryListRV.context, {
            val view: View = LayoutInflater.from(categoryListRV.context)
                    .inflate(R.layout.category_selector_item, categoryListRV, false)
            CategorySelectorViewHolder(view, selectedCategory)
        }, object : OnClickListener<Category> {
            override fun onClick(entity: Category?, `object`: Any?) {
                listener.onCategorySelected(entity!!)
            }

        })
    }

    companion object {
        @JvmStatic
        fun newInstance(listener: OnCategorySelected):
                CategorySelectorFragment {

            val fragmentBase = CategorySelectorFragment()
            val layout: Int = R.layout.category_selector_fragment

            fragmentBase.listener = listener
            fragmentBase.createBundle(layout, CategoryPresenter())
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (presenter as CategoryPresenter).getCategories({ categories: MutableList<Category> ->
            categoryList = categories

            categoryListRV.setHasFixedSize(false)
            categoryListRV.layoutManager = LinearLayoutManager(context!!)
            categoryListRV.adapter = adapter

        }, { error: Throwable ->
            error.printStackTrace()
            UIHelper.showMessage(context!!, "Error cargando categorias", error.message!!)
        })

        setupEvents()
    }

    private fun setupEvents() {
        backButton.setOnClickListener {
            removeFragment()
        }

        addCategoryButton.setOnClickListener {
            stackFragmentToTop(AddCategoryFragment.newInstance(this), R.id.mainContainer,
                    false)
        }
    }

    override fun onCategoryAdded(category: Category) {
        adapter.add(category)
    }
}