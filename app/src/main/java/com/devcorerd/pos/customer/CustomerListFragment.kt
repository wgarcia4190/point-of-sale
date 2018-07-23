package com.devcorerd.pos.customer

import android.os.Bundle
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import kotlinx.android.synthetic.main.customer_list_fragment.*

/**
 * Created by wgarcia on 7/23/2018.
 */
class CustomerListFragment: FragmentBase(){

    companion object {
        @JvmStatic
        fun newInstance():
                CustomerListFragment {
            val fragmentBase = CustomerListFragment()
            val layout: Int = R.layout.customer_list_fragment

            fragmentBase.createBundle(layout)
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
    }

    private fun setupEvents(){
        backButton.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().remove(this).commit()
        }
    }

}