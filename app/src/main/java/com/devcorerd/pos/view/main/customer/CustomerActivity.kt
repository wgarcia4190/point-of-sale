package com.devcorerd.pos.view.main.customer

import android.content.Intent
import android.os.Bundle
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.ActivityBase
import com.devcorerd.pos.helper.ConstantsHelper
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.listener.OnCustomerSelected
import com.devcorerd.pos.model.entity.Customer
import com.devcorerd.pos.view.main.home.land.HomeActivity
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard


/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
class CustomerActivity : ActivityBase(R.layout.customer_activity), OnCustomerSelected {

    private val fragment = CustomerListFragment.newInstance(this, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().replace(R.id.mainContainer,
                fragment).commit()
    }

    override fun onCustomerSelected(customer: Customer) {
        ConstantsHelper.selectedCustomer = customer
        UIHelper.startActivity(this, HomeActivity::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ConstantsHelper.scanCode -> {
                val result = data?.getStringExtra("scan_results")
                if (result != null)
                    fragment.setValuesFromOCR(result!!)
            }
            else -> {
                if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                    val scanResult = data.getParcelableExtra<CreditCard>(CardIOActivity.EXTRA_SCAN_RESULT)

                    fragment.searchByCreditCard(scanResult.cardNumber)
                }
            }
        }

    }
}