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
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.CircleAnimationHelper
import com.devcorerd.pos.listener.OnUpdateToolbarListener
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.view.main.customer.CustomerListFragment
import com.devcorerd.pos.view.main.item.ItemsFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.customer_container.*
import kotlinx.android.synthetic.main.home_activity.*

/**
 * @author Ing. Wilson Garcia
 * Created on 7/17/18
 */
class HomeActivity : ActivityBase(R.layout.home_activity),
        NavigationView.OnNavigationItemSelectedListener, OnUpdateToolbarListener {

    private var cartCounter = 0
    private var cartTotal: Double = 0.0

    private lateinit var homeFragment: HomeFragment
    private val products: MutableList<Product> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeFragment = HomeFragment.newInstance(this)
        switchFragment(homeFragment)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        navView.menu.getItem(0).isChecked = true
        setupEvents()
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
        when (item.itemId) {
            R.id.sales -> switchFragment(homeFragment)
            R.id.transactions -> {
            }
            R.id.items -> switchFragment(ItemsFragment.newInstance(this))
        }
        return true
    }

    private fun switchFragment(fragment: FragmentBase) {
        supportFragmentManager.beginTransaction().replace(R.id.container,
                fragment).commit()
    }

    private fun setupEvents() {
        customerButton.setOnClickListener {
            /*AnimationHelper.animateView(customerContainer, Techniques.ZoomIn, onStartCallback = {
                customerContainer.visibility = View.VISIBLE
            })*/
            searchCustomer.performClick()
        }

        /*customerContainer.setOnClickListener {
            if (customerContainer.visibility == View.VISIBLE) {
                AnimationHelper.animateView(customerContainer, Techniques.ZoomOut, onEndCallback = {
                    customerContainer.visibility = View.GONE
                })
            }
        }*/

        searchCustomer.setOnClickListener {
            customerContainer.performClick()
            supportFragmentManager.beginTransaction().add(R.id.mainContainer,
                    CustomerListFragment.newInstance(homeFragment)).commit()
        }
    }

    override fun onUpdateToolbar(title: String?) {
        when(title){
            null ->{
                toolbarTitle.visibility = View.GONE
                cartButtonContainer.visibility = View.VISIBLE
                customerButton.visibility = View.VISIBLE
            }
            else -> {
                toolbarTitle.visibility = View.VISIBLE
                cartButtonContainer.visibility = View.GONE
                customerButton.visibility = View.GONE

                toolbarTitle.text = title
            }
        }
    }

    fun makeFlyAnimation(imageView: CircleImageView, quantity: Int?) {
        CircleAnimationHelper().attachActivity(this).setTargetView(imageView)
                .setCircleDuration(200).setMoveDuration(400).setDestView(cartButton)
                .setAnimationListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {}

                    override fun onAnimationEnd(animation: Animator?) {
                        updateCount(quantity!!)
                        if (badge.visibility == View.GONE)
                            badge.visibility = View.VISIBLE
                    }

                    override fun onAnimationCancel(animation: Animator?) {}

                    override fun onAnimationStart(animation: Animator?) {}

                }).startAnimation()
    }

    fun updateCount(count: Int) {
        cartCounter += count
        badge.text = cartCounter.toString()
    }

    fun updateCharge(product: Product) {
        products.add(product)
        cartTotal += (product.price * product.productQuantity)
        homeFragment.updateCharge(cartTotal)

        product.productQuantity = 1
    }

}
