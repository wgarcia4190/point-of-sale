package com.devcorerd.pos.view.main.checkout

import android.os.Bundle
import android.os.Handler
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.Helper
import com.devcorerd.pos.helper.TextWatcher
import com.devcorerd.pos.model.entity.CartItem
import com.devcorerd.pos.model.enum.PaymentType
import kotlinx.android.synthetic.main.credit_card_payment_fragment.*

@Suppress("DEPRECATION")
/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
class CreditCardPaymentFragment : FragmentBase() {
    private var total: Double = 0.0
    private lateinit var items: MutableList<CartItem>

    companion object {
        @JvmStatic
        fun newInstance(total: Double, items: MutableList<CartItem>):
                CreditCardPaymentFragment {
            val fragmentBase = CreditCardPaymentFragment()
            val layout: Int = R.layout.credit_card_payment_fragment

            fragmentBase.total = total
            fragmentBase.items = items
            fragmentBase.createBundle(layout)
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        voucher.requestFocus()

        Helper.showKeyboard(activity!!)
        setupEvents()
    }

    private fun setupEvents() {
        voucher.addTextChangedListener(object : TextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)

                if (count == 0) {
                    chargeButton.isEnabled = false
                    chargeButton.setTextColor(resources.getColor(R.color.deselected))
                } else {
                    chargeButton.isEnabled = true
                    chargeButton.setTextColor(resources.getColor(R.color.white))
                }
            }
        })

        backButton.setOnClickListener {
            Helper.hideKeyboard(activity!!)

            Handler().postDelayed({
                removeFragment()
            }, 100)

        }

        chargeButton.setOnClickListener {
            Helper.hideKeyboard(activity!!)

            Handler().postDelayed({
                stackFragmentToTop(OrderCompleteFragment.newInstance(total,
                        total, PaymentType.CREDIT_CARD, items, voucher.text.toString()
                ), R.id.mainContainer,
                        false)
            }, 100)
        }
    }
}