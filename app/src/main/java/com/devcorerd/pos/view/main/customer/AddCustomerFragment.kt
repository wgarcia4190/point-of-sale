package com.devcorerd.pos.view.main.customer

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.TextWatcher
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.listener.OnCustomerAddedListener
import com.devcorerd.pos.model.entity.Customer
import com.devcorerd.pos.model.presenter.CustomerPresenter
import kotlinx.android.synthetic.main.add_customer_fragment.*

@Suppress("DEPRECATION")
/**
 * @author Ing. Wilson Garcia
 * Created on 7/24/18
 */
class AddCustomerFragment : FragmentBase() {

    private lateinit var listener: OnCustomerAddedListener

    companion object {
        @JvmStatic
        fun newInstance(listener: OnCustomerAddedListener):
                AddCustomerFragment {
            val fragmentBase = AddCustomerFragment()
            val layout: Int = R.layout.add_customer_fragment

            fragmentBase.listener = listener
            fragmentBase.createBundle(layout, CustomerPresenter())
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEvents()
    }

    private fun setupEvents() {
        setupTextWatcher(name, lastName, email, socialID)

        backButton.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().remove(this).commit()
        }

        addCustomerButton.setOnClickListener {
            val customer = Customer(name.text.toString(), lastName.text.toString(),
                    socialID.text.toString(), email.text.toString())

            (presenter as CustomerPresenter).addCustomer(customer, {
                listener.onCustomerAdded(customer)

                UIHelper.clearEditText(name, lastName, email, socialID)
                backButton.performClick()
            }, { error: Throwable ->
                print(error.message)
            })
        }
    }

    private fun setupTextWatcher(vararg editTexts: EditText) {
        for (editText in editTexts)
            editText.addTextChangedListener(object : TextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    super.afterTextChanged(s)

                    var activateButton = true
                    for (innerEditText in editTexts) {
                        if (innerEditText.text.toString().isEmpty()) {
                            activateButton = false
                            break
                        }
                    }

                    if (activateButton) {
                        addCustomerButton.isEnabled = true
                        addCustomerButton.setTextColor(resources.getColor(R.color.white))
                    } else {
                        addCustomerButton.isEnabled = false
                        addCustomerButton.setTextColor(resources.getColor(R.color.deselected))
                    }
                }
            })
    }
}