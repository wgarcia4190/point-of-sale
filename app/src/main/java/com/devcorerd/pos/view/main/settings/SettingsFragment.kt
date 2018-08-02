package com.devcorerd.pos.view.main.settings

import android.os.Bundle
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.listener.OnUpdateToolbarListener
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
    }
}