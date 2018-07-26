package com.devcorerd.pos.view.main.splash

import android.os.Bundle
import android.os.Handler
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.ActivityBase
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.model.entity.Category
import com.devcorerd.pos.model.presenter.CategoryPresenter
import com.devcorerd.pos.view.main.login.LoginActivity
import org.joda.time.DateTime

/**
 * Created by wgarcia on 7/16/2018.
 */
class SplashScreenActivity : ActivityBase(layout = R.layout.splashscreen_activity,
        presenter = CategoryPresenter(),
        isFullScreen = true) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({
            try {
                (presenter as CategoryPresenter).getCategory { category: Category? ->
                    if (category == null) {
                        val newCategory = Category("Sin Asignar", "#AAAAAA",
                                DateTime.now(), DateTime.now())
                        (presenter as CategoryPresenter).addCategory(newCategory, {
                            UIHelper.startActivity(this, LoginActivity::class.java)
                        }, { error: Throwable ->
                            error.printStackTrace()
                        })
                    } else
                        UIHelper.startActivity(this, LoginActivity::class.java)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }, 500)

    }
}