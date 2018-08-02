package com.devcorerd.pos.view.main.checkout

import android.os.Bundle
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.model.entity.CartItem
import kotlinx.android.synthetic.main.checkout_fragment.*

/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
class CheckoutFragment : FragmentBase() {

    private var total: Double = 0.0
    private lateinit var items: MutableList<CartItem>

    companion object {
        @JvmStatic
        fun newInstance(total: Double, items: MutableList<CartItem>):
                CheckoutFragment {
            val fragmentBase = CheckoutFragment()
            val layout: Int = R.layout.checkout_fragment

            fragmentBase.total = total
            fragmentBase.items = items
            fragmentBase.createBundle(layout)
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        totalText.text = "$".plus(String.format("%.2f", total))
        setupEvents()
    }

    private fun setupEvents() {
        closeButton.setOnClickListener {
            removeFragment()
        }

        cashType.setOnClickListener {
            stackFragmentToTop(CashPaymentFragment.newInstance(total, items), R.id.mainContainer,
                    false)
        }

        creditCardType.setOnClickListener {
            stackFragmentToTop(CreditCardPaymentFragment.newInstance(total, items), R.id.mainContainer,
                    false)
        }


    }

}