package com.devcorerd.pos.core.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devcorerd.pos.R
import com.devcorerd.pos.core.Application
import com.devcorerd.pos.core.api.Presenter
import com.devcorerd.pos.core.exception.ViewCannotBeNullException
import com.devcorerd.pos.helper.ConstantsHelper
import com.google.android.gms.analytics.Tracker

/**
 * Created by wgarcia on 7/16/2018.
 */
open class FragmentBase: Fragment() {

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

    protected fun addFragmentToStack(fragment: FragmentBase) {
        addFragmentToStack(fragment, R.id.container)
    }

    protected fun addFragmentToStack(fragment: FragmentBase, layout: Int) {
        addFragmentToStack(fragment, layout, true)
    }

    protected fun addFragmentToStack(fragment: FragmentBase, layout: Int, hideThis: Boolean) {
        val transaction: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()

        transaction.addToBackStack(null)
        if(hideThis)
            transaction.hide(this)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(layout, fragment, fragment.tag).commit()
    }

    protected fun stackFragmentToTop(fragment: FragmentBase, layout: Int, hideThis: Boolean) {
        val transaction: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()

        transaction.addToBackStack(null)

        if(hideThis)
            transaction.hide(this)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.add(layout, fragment, fragment.tag).commit()
    }

    protected fun stackFragmentToTop(fragment: Fragment, layout: Int, hideThis: Boolean) {
        val transaction: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()

        transaction.addToBackStack(null)

        if(hideThis)
            transaction.hide(this)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.add(layout, fragment, fragment.tag).commit()
    }

    protected fun removeFragment(){
        activity!!.supportFragmentManager.beginTransaction().remove(this).commit()
    }

    override fun onResume() {
        super.onResume()

        if (view == null) {
            return
        }

        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener { _, keyCode, event ->
            if(event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                activity!!.supportFragmentManager.beginTransaction().remove(this@FragmentBase).commit()
                true
            }else
                false
        }
    }
}