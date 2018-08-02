package com.devcorerd.pos.view.main.customer

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.EditText
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.ConstantsHelper
import com.devcorerd.pos.helper.TextWatcher
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.listener.OnCustomerAddedListener
import com.devcorerd.pos.model.entity.Customer
import com.devcorerd.pos.model.presenter.CustomerPresenter
import com.devcorerd.pos.view.main.ocr.OCRCaptureActivity
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
        setupTextWatcher(name, lastName, email, socialID, card)

        backButton.setOnClickListener {
            removeFragment()
        }

        addCustomerButton.setOnClickListener {
            val customer = Customer(name.text.toString(), lastName.text.toString(),
                    socialID.text.toString(), email.text.toString(), card.text.toString())

            (presenter as CustomerPresenter).addCustomer(customer, {
                listener.onCustomerAdded(customer)

                UIHelper.clearEditText(name, lastName, email, socialID, card)
                backButton.performClick()
            }, { error: Throwable ->
                print(error.message)
            })
        }

        ocrReaderButton.setOnClickListener {
            UIHelper.startSubActivityForResult(activity!!, OCRCaptureActivity::class.java, ConstantsHelper.scanCode)
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

    fun setValuesFromOCR(value: String) {
        val blocksRaw = value.split("\n")
        val blocksClean: MutableList<String> = mutableListOf()

        for (block in blocksRaw) {
            if (block.toLowerCase().contains("junta", true) ||
                    block.toLowerCase().contains("identidad", true) ||
                    block.toLowerCase().contains("repu", true) ||
                    block.toLowerCase().contains("dominicana", true) ||
                    block.toLowerCase().contains("lugar", true) ||
                    block.toLowerCase().contains("fecha", true) ||
                    block.toLowerCase().contains("nacionalidad", true) ||
                    block.toLowerCase().contains("elect", true) ||
                    block.toLowerCase().contains("ocup", true) ||
                    block.toLowerCase().contains("expira", true) ||
                    block.toLowerCase().contains("r.d.", true) ||
                    block.toLowerCase().contains("sexo", true) ||
                    block.toLowerCase().contains("B-", true) ||
                    block.toLowerCase().contains("B+", true) ||
                    block.toLowerCase().contains("A+", true) ||
                    block.toLowerCase().contains("A-", true) ||
                    block.toLowerCase().contains("O+", true) ||
                    block.toLowerCase().contains("O-", true) ||
                    block.toLowerCase().contains("estado", true) ||
                    block.toLowerCase().contains(":", true) ||
                    block.toLowerCase().contains(",", true) ||
                    (block.toLowerCase().contains(Regex("\\d")) && !block.contains("-")) ||
                    block.isEmpty()) {
                continue
            } else {
                blocksClean.add(block)
            }
        }

        for (block in blocksClean) {
            if (block.toLowerCase().contains(Regex("\\d")) && block.contains("-"))
                socialID.setText(block.replace("-", ""))
            else if (name.text.isEmpty())
                name.setText(block)
            else
                lastName.setText(block)
        }

    }
}