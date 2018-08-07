package com.devcorerd.pos.view.main.home.land

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.ActivityBase
import com.devcorerd.pos.listener.OnToggleDrawer
import com.devcorerd.pos.listener.OnUpdateToolbarListener
import com.devcorerd.pos.model.presenter.ProductPresenter
import kotlinx.android.synthetic.main.home_activity.*

/**
 * @author Ing. Wilson Garcia
 * Created on 8/7/18
 */
class HomeActivity: ActivityBase(R.layout.home_activity, ProductPresenter()),
        NavigationView.OnNavigationItemSelectedListener, OnUpdateToolbarListener, OnToggleDrawer {


    private lateinit var homeFragment: HomeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeFragment = HomeFragment.newInstance(this, this)
        switchFragment(homeFragment)

        setSupportActionBar(landToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, landToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        navView.menu.getItem(0).isChecked = true

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.sales -> switchFragment(homeFragment)
        }
        return false
    }

    override fun onUpdateToolbar(title: String?) {
        when(title){
            null -> landToolbar.visibility = View.GONE
            else -> {
                landToolbar.visibility = View.VISIBLE
            }
        }
    }

    override fun onToggleDrawer() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            drawerLayout.openDrawer(GravityCompat.START)
    }

}