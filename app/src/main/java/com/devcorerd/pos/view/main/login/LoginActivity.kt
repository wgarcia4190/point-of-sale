package com.devcorerd.pos.view.main.login

import android.os.Bundle
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.ActivityBase

/**
 * @author Ing. Wilson Garcia
 * Created on 7/16/18
 */
class LoginActivity: ActivityBase(layout = R.layout.login_activity, isFullScreen = true){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().replace(R.id.container,
                LoginOptionsFragment.newInstance()).commit()
    }
}