package com.devcorerd.pos.view.main.login

import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase

/**
 * Created by wgarcia on 7/17/2018.
 */
class LoginFragment: FragmentBase(){

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

}