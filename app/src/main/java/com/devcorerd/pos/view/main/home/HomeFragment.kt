package com.devcorerd.pos.view.main.home

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.ViewPagerAdapter
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.core.ui.Tab
import com.devcorerd.pos.helper.ConstantsHelper
import com.devcorerd.pos.listener.OnCustomerSelected
import com.devcorerd.pos.listener.OnUpdateToolbarListener
import com.devcorerd.pos.model.entity.Customer
import com.devcorerd.pos.view.main.checkout.CheckoutFragment
import kotlinx.android.synthetic.main.customer_header.*
import kotlinx.android.synthetic.main.home_fragment.*

@Suppress("DEPRECATION", "PLUGIN_WARNING")
/**
 * @author Ing. Wilson Garcia
 * Created on 7/17/18
 */
class HomeFragment : FragmentBase(), OnCustomerSelected {

    private lateinit var listener: OnUpdateToolbarListener
    private var price: Double = 0.0
    private val tabs: MutableList<Tab> by lazy {
        mutableListOf(
                Tab(getString(R.string.all), R.drawable.ic_list),
                Tab(getString(R.string.favorites), R.drawable.ic_favorite))
    }

    companion object {
        @JvmStatic
        fun newInstance(listener: OnUpdateToolbarListener):
                HomeFragment {
            val fragmentBase = HomeFragment()
            val layout: Int = R.layout.home_fragment

            fragmentBase.listener = listener
            fragmentBase.createBundle(layout)
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        setupEvents()

        listener.onUpdateToolbar(null)
        onCustomerSelected(ConstantsHelper.selectedCustomer!!)
    }

    private fun setupViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPagerAdapter.addFragment(AllItemsFragment.newInstance(), tabs[0].text)
        viewPagerAdapter.addFragment(FavoriteItemsFragment.newInstance(), tabs[1].text)

        viewPager.adapter = viewPagerAdapter
        viewPager.offscreenPageLimit = 2

        mainTabLayout.setupWithViewPager(viewPager, true)

        setupTabLabel()
    }

    private fun setupTabLabel() {
        for ((index, tabObject) in tabs.withIndex()) {
            val tab = LayoutInflater.from(this.context!!).inflate(R.layout.tab, null)
                    as LinearLayout
            val tabLabel = tab.findViewById(R.id.tabText) as TextView
            tabLabel.text = tabObject.text

            val tabIcon = tab.findViewById(R.id.tabIcon) as ImageView
            tabIcon.setImageDrawable(context!!.resources.getDrawable(tabObject.image))

            mainTabLayout.getTabAt(index)!!.customView = tab
            tab.isSelected = true

            if (index == 0)
                updateTab(mainTabLayout.getTabAt(index)!!, R.color.colorPrimary)
        }
    }

    private fun setupEvents() {
        mainTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                updateTab(tab, R.color.deselected)
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                updateTab(tab, R.color.colorPrimary)

            }

        })

        chargeButton.setOnClickListener {
            stackFragmentToTop(CheckoutFragment.newInstance(price,
                    (activity!! as HomeActivity).products), R.id.mainContainer,
                    false)
        }
    }

    private fun updateTab(tab: TabLayout.Tab?, color: Int) {
        val customTab: LinearLayout = tab?.customView as LinearLayout

        val tabLabel = customTab.findViewById(R.id.tabText) as TextView
        tabLabel.setTextColor(ContextCompat.getColor(activity!!, color))

        val tabIcon = customTab.findViewById(R.id.tabIcon) as ImageView
        tabIcon.setColorFilter(ContextCompat.getColor(activity!!, color),
                PorterDuff.Mode.SRC_IN)
    }

    fun updateCharge(price: Double) {
        this.price = price
        val charge: String = resources.getString(R.string.charge_wildcard)
                .replace("{price}", String.format("%.2f", price))

        chargeButton.text = charge
        if (price > 0.0) {
            chargeButton.isEnabled = true
            chargeButton.setTextColor(resources.getColor(R.color.white))
        } else {
            chargeButton.isEnabled = false
            chargeButton.setTextColor(resources.getColor(R.color.deselected))
        }

    }

    override fun onCustomerSelected(customer: Customer) {
        customerHeader.visibility = View.VISIBLE
        customerName.text = customer.getFullName()
        customerSocialID.text = customer.socialID
        customerEmail.text = customer.email
    }
}