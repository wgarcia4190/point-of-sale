package com.devcorerd.pos.core.api

import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Created by wgarcia on 7/16/2018.
 */
abstract class Presenter{
    private val compositeSubscription: CompositeSubscription? = CompositeSubscription()
    private lateinit var context: AppCompatActivity
    private lateinit var supportFragmentManager: FragmentManager

    protected fun manageSubscription(subscription: Subscription, showLoading: Boolean = true, validateToken: Boolean = true) {

            runService(subscription, showLoading)


    }

    private fun runService(subscription: Subscription, showLoading: Boolean) {
        compositeSubscription?.add(subscription)
    }

    open fun initService(context: AppCompatActivity?) {
        this.context = context!!
        supportFragmentManager = context.supportFragmentManager
    }
}