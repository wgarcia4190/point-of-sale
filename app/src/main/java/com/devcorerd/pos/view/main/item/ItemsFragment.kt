package com.devcorerd.pos.view.main.item

import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase

/**
 * Created by wgarcia on 7/24/2018.
 */
class ItemsFragment: FragmentBase(){

    companion object {
        @JvmStatic
        fun newInstance():
                ItemsFragment {
            val fragmentBase = ItemsFragment()
            val layout: Int = R.layout.items_fragment

            fragmentBase.createBundle(layout)
            return fragmentBase
        }
    }
}