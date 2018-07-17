package com.devcorerd.pos.view.main.login

import android.os.Bundle
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import kotlinx.android.synthetic.main.login_options_fragment.*

/**
 * @author Ing. Wilson Garcia
 * Created on 7/16/18
 */
class LoginOptionsFragment: FragmentBase(){

    companion object {
        @JvmStatic
        fun newInstance():
                LoginOptionsFragment {
            val fragmentBase = LoginOptionsFragment()
            val layout: Int = R.layout.login_options_fragment

            fragmentBase.createBundle(layout)
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()

    }

    private fun setupEvents(){
        login.setOnClickListener {
            addFragmentToStack(LoginFragment.newInstance())
        }
    }

}