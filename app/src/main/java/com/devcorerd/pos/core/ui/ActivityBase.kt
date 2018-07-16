package com.devcorerd.pos.core.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.devcorerd.pos.core.Application
import com.devcorerd.pos.core.api.Presenter
import com.google.android.gms.analytics.Tracker

/**
 * Created by wgarcia on 7/16/2018.
 */
open class ActivityBase(private val layout: Int, protected var presenter: Presenter? = null,
                        private val isFullScreen: Boolean = false):
        AppCompatActivity(){

    constructor(layout: Int, isFullScreen: Boolean): this(layout, null, isFullScreen)

    protected lateinit var tracker: Tracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tracker = Application.get().getDefaultTracker()

        if(isFullScreen)
            hideSystemUI()

        presenter?.initService(this)
        setContentView(layout)
    }

    private fun hideSystemUI(){
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}