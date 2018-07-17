package com.devcorerd.pos.view.main.splash

import android.os.Bundle
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.ActivityBase
import com.devcorerd.pos.helper.Helper
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.view.main.login.LoginActivity

/**
 * Created by wgarcia on 7/16/2018.
 */
class SplashScreenActivity : ActivityBase(layout = R.layout.splashscreen_activity,
        isFullScreen = true) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Helper.isInternetAvailable(this)
        UIHelper.startActivity(this, LoginActivity::class.java)
    }
}