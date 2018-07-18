package com.devcorerd.pos.view.main.login

import android.os.Bundle
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.view.main.home.HomeActivity
import kotlinx.android.synthetic.main.login_fragment.*

/**
 * Created by wgarcia on 7/17/2018.
 */
class LoginFragment : FragmentBase() {

    companion object {
        @JvmStatic
        fun newInstance():
                LoginFragment {
            val fragmentBase = LoginFragment()
            val layout: Int = R.layout.login_fragment

            fragmentBase.createBundle(layout)
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEvents()
    }

    private fun setupEvents() {
        login.setOnClickListener {
            UIHelper.startActivity(activity!!, HomeActivity::class.java)
        }
    }

}