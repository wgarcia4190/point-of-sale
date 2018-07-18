package com.devcorerd.pos.view.main.home

import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase

/**
 * @author Ing. Wilson Garcia
 * Created on 7/17/18
 */
class AllItemsFragment: FragmentBase(){

    companion object {
        @JvmStatic
        fun newInstance():
                AllItemsFragment {
            val fragmentBase = AllItemsFragment()
            val layout: Int = R.layout.all_items_fragment

            fragmentBase.createBundle(layout)
            return fragmentBase
        }
    }

}