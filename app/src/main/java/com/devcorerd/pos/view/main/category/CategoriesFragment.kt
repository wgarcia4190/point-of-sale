package com.devcorerd.pos.view.main.category

import android.view.LayoutInflater
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.Adapter
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.Category
import com.devcorerd.pos.model.presenter.CategoryPresenter
import com.devcorerd.pos.view.viewholder.CategorySelectorViewHolder
import kotlinx.android.synthetic.main.categories_fragment.*

/**
 * @author Ing. Wilson Garcia
 * Created on 7/29/18
 */
class CategoriesFragment: FragmentBase(){

    private lateinit var categoryList: MutableList<Category>
    private val adapter: Adapter<Category, CategorySelectorViewHolder> by lazy {
        Adapter(categoryList, categoryListRV.context, {
            val view: View = LayoutInflater.from(categoryListRV.context)
                    .inflate(R.layout.category_selector_item, categoryListRV, false)
            CategorySelectorViewHolder(view, null)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance():
                CategoriesFragment {
            val fragmentBase = CategoriesFragment()
            val layout: Int = R.layout.products_fragment

            fragmentBase.createBundle(layout, CategoryPresenter())
            return fragmentBase
        }
    }

}