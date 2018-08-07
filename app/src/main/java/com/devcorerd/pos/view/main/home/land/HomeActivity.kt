package com.devcorerd.pos.view.main.home.land

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.ActivityBase
import com.devcorerd.pos.model.presenter.ProductPresenter
import kotlinx.android.synthetic.main.home_activity.*

/**
 * @author Ing. Wilson Garcia
 * Created on 8/7/18
 */
class HomeActivity: ActivityBase(R.layout.home_activity, ProductPresenter()),
        NavigationView.OnNavigationItemSelectedListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(landToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, landToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        navView.menu.getItem(0).isChecked = true

        landToolbar.visibility = View.GONE
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        return false
    }

}