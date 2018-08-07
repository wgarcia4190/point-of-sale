package com.devcorerd.pos.view.main.login

import android.os.Bundle
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.ConstantsHelper
import com.devcorerd.pos.helper.PreferencesHelper
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.view.main.customer.CustomerActivity
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
            if(email.text.toString() == "admin@adess.com" && password.text.toString() == "123456") {
                UIHelper.startActivity(activity!!, CustomerActivity::class.java)
                PreferencesHelper.instance.saveBoolean(ConstantsHelper.logInKey, true)
            }else
                UIHelper.showMessage(context!!, "Error de Autenticación", "Las credenciales introducidas no son correctas")

        }
    }

}