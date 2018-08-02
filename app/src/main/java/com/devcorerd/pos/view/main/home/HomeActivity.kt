package com.devcorerd.pos.view.main.home

import android.Manifest
import android.animation.Animator
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.ActivityBase
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.CircleAnimationHelper
import com.devcorerd.pos.helper.ConstantsHelper
import com.devcorerd.pos.helper.Helper
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.listener.OnCartItemDeleted
import com.devcorerd.pos.listener.OnScanCompleted
import com.devcorerd.pos.listener.OnUpdateToolbarListener
import com.devcorerd.pos.model.entity.CartItem
import com.devcorerd.pos.model.entity.Product
import com.devcorerd.pos.model.presenter.ProductPresenter
import com.devcorerd.pos.view.main.barcode.BarcodeReaderFragment
import com.devcorerd.pos.view.main.checkout.CartFragment
import com.devcorerd.pos.view.main.customer.CustomerListFragment
import com.devcorerd.pos.view.main.item.ItemsFragment
import com.devcorerd.pos.view.main.settings.SettingsFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.customer_container.*
import kotlinx.android.synthetic.main.home_activity.*
import me.dm7.barcodescanner.zbar.Result

/**
 * @author Ing. Wilson Garcia
 * Created on 7/17/18
 */
class HomeActivity : ActivityBase(R.layout.home_activity, ProductPresenter()),
        NavigationView.OnNavigationItemSelectedListener, OnUpdateToolbarListener, OnCartItemDeleted {

    private var cartCounter = 0
    private var cartTotal: Double = 0.0

    private lateinit var homeFragment: HomeFragment
    val products: MutableList<CartItem> = mutableListOf()

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
            R.id.items -> switchFragment(ItemsFragment.newInstance(this))
            R.id.settings -> switchFragment(SettingsFragment.newInstance(this))
        }
        return true
    }

    private fun switchFragment(fragment: FragmentBase) {
        supportFragmentManager.beginTransaction().replace(R.id.container,
                fragment).commit()
    }

    private fun setupEvents() {
        customerButton.setOnClickListener {
            searchCustomer.performClick()
        }

        barReaderButton.setOnClickListener {
            if (!Helper.checkPermissions(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), ConstantsHelper.cameraCode)
            } else
                openBarCodeReader()
        }

        searchCustomer.setOnClickListener {
            customerContainer.performClick()
            supportFragmentManager.beginTransaction().add(R.id.mainContainer,
                    CustomerListFragment.newInstance(homeFragment)).commit()
        }

        cartButtonContainer.setOnClickListener {
            if (products.isNotEmpty())
                supportFragmentManager.beginTransaction().add(R.id.mainContainer,
                        CartFragment.newInstance(products, this)).commit()
        }
    }

    private fun openBarCodeReader() {
        supportFragmentManager.beginTransaction().add(R.id.mainContainer,
                BarcodeReaderFragment.newInstance(object : OnScanCompleted {
                    override fun onScanComple(barcode: Result?) {
                        (presenter as ProductPresenter).getProductByBarCode(barcode?.contents!!, { product: Product? ->

                            if (product != null) {
                                updateCount(1)
                                updateCharge(product)
                                if (badge.visibility == View.GONE)
                                    badge.visibility = View.VISIBLE
                            } else
                                UIHelper.showMessage(this@HomeActivity,
                                        "Producto no encontrado",
                                        "No se encontro ningún producto registrado " +
                                                "con este código de barra")


                        }, { error: Throwable ->
                            UIHelper.showMessage(this@HomeActivity,
                                    "Error cargando producto",
                                    error.message!!)
                        })

                    }

                })).commit()
    }

    override fun onUpdateToolbar(title: String?) {
        when (title) {
            null -> {
                toolbarTitle.visibility = View.GONE
                cartButtonContainer.visibility = View.VISIBLE
                customerButton.visibility = View.VISIBLE
                barReaderButton.visibility = View.VISIBLE
            }
            else -> {
                toolbarTitle.visibility = View.VISIBLE
                cartButtonContainer.visibility = View.GONE
                customerButton.visibility = View.GONE
                barReaderButton.visibility = View.GONE

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

        if (count == 0)
            badge.visibility = View.GONE
    }

    fun updateCharge(product: Product) {
        val cartItem = CartItem(product, product.productQuantity)

        if (!products.contains(cartItem))
            products.add(cartItem)
        else {
            val index = products.indexOf(cartItem)
            cartItem.quantity = products[index].quantity + product.productQuantity
            products[index] = cartItem
        }
        cartTotal += (product.price * product.productQuantity)
        homeFragment.updateCharge(cartTotal)

        product.productQuantity = 1
    }

    fun updateCharge(cartItem: CartItem, difference: Int) {
        val index = products.indexOf(cartItem)
        products[index] = cartItem

        cartTotal += (cartItem.product.price * (cartItem.quantity - difference))
        homeFragment.updateCharge(cartTotal)
    }

    override fun onAllProductsDeleted() {
        updateCount(0)
        cartTotal = 0.0
        homeFragment.updateCharge(cartTotal)
        products.clear()
    }

    override fun onProductDeleted(cartItem: CartItem) {
        cartCounter -= cartItem.quantity
        badge.text = cartCounter.toString()

        if (cartCounter == 0)
            badge.visibility = View.GONE

        products.remove(cartItem)
        cartTotal -= (cartItem.product.price * cartItem.quantity)
        homeFragment.updateCharge(cartTotal)


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {

            ConstantsHelper.cameraCode -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    UIHelper.showMessage(this, "Error",
                            getString(R.string.not_file_permission_granted))
                else
                    openBarCodeReader()

            }
        }
    }

}
