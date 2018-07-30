package com.devcorerd.pos.view.main.item

import android.os.Bundle
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.listener.OnUpdateToolbarListener
import com.devcorerd.pos.view.main.category.CategoriesFragment
import kotlinx.android.synthetic.main.items_fragment.*

/**
 * Created by wgarcia on 7/24/2018.
 */
class ItemsFragment : FragmentBase() {

    private lateinit var listener: OnUpdateToolbarListener

    companion object {
        @JvmStatic
        fun newInstance(listener: OnUpdateToolbarListener):
                ItemsFragment {
            val fragmentBase = ItemsFragment()
            val layout: Int = R.layout.items_fragment

            fragmentBase.listener = listener
            fragmentBase.createBundle(layout)
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener.onUpdateToolbar(getString(R.string.items))
        setupEvents()
    }

    private fun setupEvents() {
        productsButton.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().add(R.id.mainContainer,
                    ProductsFragment.newInstance()).commit()
        }

        categoriesButton.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().add(R.id.mainContainer,
                    CategoriesFragment.newInstance()).commit()
        }
    }
}