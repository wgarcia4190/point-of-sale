package com.devcorerd.pos.view.main.category

import android.os.Bundle
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.model.entity.Category
import com.devcorerd.pos.model.presenter.CategoryPresenter
import kotlinx.android.synthetic.main.category_details_fragment.*

/**
 * @author Ing. Wilson Garcia
 * Created on 7/30/18
 */
class CategoryDetailFragment : FragmentBase() {

    private lateinit var selectedCategory: Category

    companion object {
        @JvmStatic
        fun newInstance(category: Category):
                CategoryDetailFragment {
            val fragmentBase = CategoryDetailFragment()
            val layout: Int = R.layout.category_details_fragment

            fragmentBase.selectedCategory = category
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
    }

    private fun setupEvents(){
        backButton.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().remove(this).commit()
        }
    }

}