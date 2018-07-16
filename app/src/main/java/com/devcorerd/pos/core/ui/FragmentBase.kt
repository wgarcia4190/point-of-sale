package com.devcorerd.pos.core.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devcorerd.pos.core.Application
import com.devcorerd.pos.core.api.Presenter
import com.devcorerd.pos.core.exception.ViewCannotBeNullException
import com.devcorerd.pos.helper.ConstantsHelper
import com.google.android.gms.analytics.Tracker

/**
 * Created by wgarcia on 7/16/2018.
 */
class FragmentBase: Fragment() {

    protected lateinit var tracker: Tracker
    protected var presenter: Presenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        tracker = Application.get().getDefaultTracker()

        val layout: Int = arguments!!.getInt(ConstantsHelper.layoutKey, 0)
        if (layout == 0)
            throw ViewCannotBeNullException()

        return inflater.inflate(layout, container, false)
    }

    protected fun createBundle(layout: Int, presenterObj: Presenter? = null): Bundle {
        val bundle = Bundle()
        bundle.putInt(ConstantsHelper.layoutKey, layout)
        arguments = bundle
        presenter = presenterObj

        return bundle
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter?.initService(activity as AppCompatActivity?)
    }
}