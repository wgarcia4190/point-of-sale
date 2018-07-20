package com.devcorerd.pos.view.main.home

import android.animation.Animator
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.ActivityBase
import com.devcorerd.pos.helper.CircleAnimationHelper
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.home_activity.*

/**
 * @author Ing. Wilson Garcia
 * Created on 7/17/18
 */
class HomeActivity : ActivityBase(R.layout.home_activity),
        NavigationView.OnNavigationItemSelectedListener {

    private var cartCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().replace(R.id.container,
                HomeFragment.newInstance()).commit()

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun makeFlyAnimation(imageView: CircleImageView){
        CircleAnimationHelper().attachActivity(this).setTargetView(imageView)
                .setCircleDuration(200).setMoveDuration(400).setDestView(cartButton)
                .setAnimationListener(object : Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) {
                        updateCount(1)
                        if(badge.visibility == View.GONE)
                            badge.visibility = View.VISIBLE
                    }

                    override fun onAnimationCancel(animation: Animator?) {}

                    override fun onAnimationStart(animation: Animator?) {}

                }).startAnimation()
    }

    fun updateCount(count: Int){
        cartCounter += count
        badge.text = cartCounter.toString()
    }

}
