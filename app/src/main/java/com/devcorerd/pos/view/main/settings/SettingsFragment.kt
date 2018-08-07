package com.devcorerd.pos.view.main.settings

import android.os.Bundle
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.ConstantsHelper
import com.devcorerd.pos.helper.PreferencesHelper
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.listener.OnUpdateToolbarListener
import com.devcorerd.pos.view.main.login.LoginActivity
import kotlinx.android.synthetic.main.settings_fragment.*

/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
class SettingsFragment : FragmentBase() {
    private lateinit var listener: OnUpdateToolbarListener

    companion object {
        @JvmStatic
        fun newInstance(listener: OnUpdateToolbarListener):
                SettingsFragment {
            val fragmentBase = SettingsFragment()
            val layout: Int = R.layout.settings_fragment

            fragmentBase.listener = listener
            fragmentBase.createBundle(layout)
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener.onUpdateToolbar(getString(R.string.settings))
        setupEvents()
    }

    private fun setupEvents() {
        printersButton.setOnClickListener {
            stackFragmentToTop(PrintersFragment.newInstance(), R.id.mainContainer,
                    false)
        }

        logout.setOnClickListener {
            UIHelper.showMessage(context!!, resources.getString(R.string.logout_questions), "Cerrar Sesión", {
                PreferencesHelper.instance.saveBoolean(ConstantsHelper.logInKey, false)
                UIHelper.startActivity(activity!!, LoginActivity::class.java)
            }, "Si", "No")
        }
    }
}