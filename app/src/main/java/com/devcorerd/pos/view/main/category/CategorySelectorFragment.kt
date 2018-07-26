package com.devcorerd.pos.view.main.category

import android.os.Bundle
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.listener.OnCategorySelected
import kotlinx.android.synthetic.main.category_selector_fragment.*

/**
 * Created by wgarcia on 7/26/2018.
 */
class CategorySelectorFragment: FragmentBase(){

    private lateinit var listener: OnCategorySelected

    companion object {
        @JvmStatic
        fun newInstance(listener: OnCategorySelected):
                CategorySelectorFragment {

            val fragmentBase = CategorySelectorFragment()
            val layout: Int = R.layout.add_product_fragment

            fragmentBase.listener = listener
            fragmentBase.createBundle(layout)
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupEvents(){
        backButton.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().remove(this).commit()
        }
    }
}