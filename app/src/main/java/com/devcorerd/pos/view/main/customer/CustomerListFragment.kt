package com.devcorerd.pos.view.main.customer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.Adapter
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.ConstantsHelper
import com.devcorerd.pos.helper.Helper
import com.devcorerd.pos.helper.TextWatcher
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.listener.OnCustomerAddedListener
import com.devcorerd.pos.listener.OnCustomerSelected
import com.devcorerd.pos.listener.OnScanCompleted
import com.devcorerd.pos.model.entity.Customer
import com.devcorerd.pos.model.presenter.CustomerPresenter
import com.devcorerd.pos.view.main.barcode.BarcodeReaderFragment
import com.devcorerd.pos.view.viewholder.CustomerListViewHolder
import io.card.payment.CardIOActivity
import kotlinx.android.synthetic.main.customer_list_fragment.*
import me.dm7.barcodescanner.zbar.Result


/**
 * Created by wgarcia on 7/23/2018.
 */
class CustomerListFragment : FragmentBase(), OnCustomerAddedListener, OnScanCompleted {
    private lateinit var customerList: MutableList<Customer>
    private var tempCustomerList: MutableList<Customer> = mutableListOf()
    private var showBack: Boolean = true

    private lateinit var listener: OnCustomerSelected
    private val fragment = AddCustomerFragment.newInstance(this)

    private val adapter: Adapter<Customer, CustomerListViewHolder> by lazy {
        Adapter(customerList, customerRVList.context, {
            val view: View = LayoutInflater.from(customerRVList.context)
                    .inflate(R.layout.customer_list_item, customerRVList, false)
            CustomerListViewHolder(view)
        }, object : OnClickListener<Customer> {
            override fun onClick(entity: Customer?, `object`: Any?) {
                listener.onCustomerSelected(entity!!)
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(listener: OnCustomerSelected, showBack: Boolean):
                CustomerListFragment {
            val fragmentBase = CustomerListFragment()
            val layout: Int = R.layout.customer_list_fragment

            fragmentBase.listener = listener
            fragmentBase.showBack = showBack
            fragmentBase.createBundle(layout, CustomerPresenter())
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        load()
        setupEvents()

        if(showBack)
            backButton.visibility = View.VISIBLE
        else
            backButton.visibility = View.GONE
    }

    private fun setupEvents() {
        backButton.setOnClickListener {
            removeFragment()
        }

        clearSearch.setOnClickListener {
            searchCustomer.setText("")
            it.visibility = View.GONE
            adapter.swap(customerList)
        }

        barReaderButton.setOnClickListener {
            if (!Helper.checkPermissions(context!!, Manifest.permission.CAMERA)) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA),
                        ConstantsHelper.cameraCode)
            } else
                stackFragmentToTop(BarcodeReaderFragment.newInstance(this), R.id.mainContainer,
                        false)
        }

        searchCustomer.addTextChangedListener(object : TextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)

                tempCustomerList.clear()

                val text = s.toString()
                if (count == 0) {
                    clearSearch.visibility = View.GONE
                    adapter.swap(customerList)

                    return
                } else
                    clearSearch.visibility = View.VISIBLE

                for (customer in customerList) {
                    if (text.matches(Regex("[0-9]+"))) {
                        if (customer.socialID.contains(text) || customer.card.contains(text))
                            tempCustomerList.add(customer)
                    } else if (customer.getFullName().contains(text) ||
                            customer.email.contains(text))
                        tempCustomerList.add(customer)
                }

                adapter.swap(tempCustomerList)
                toggleList(tempCustomerList, customerRVList)
            }
        })

        addCustomerButton.setOnClickListener {
            stackFragmentToTop(fragment, R.id.mainContainer, false)
        }

        creditCardButton.setOnClickListener {
            val scanIntent = Intent(activity!!, CardIOActivity::class.java)

            scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_INSTRUCTIONS, false)
            scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true)
            scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, true)
            scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true)
            scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true)
            scanIntent.putExtra(CardIOActivity.EXTRA_SCAN_INSTRUCTIONS, "")
            scanIntent.putExtra(CardIOActivity.EXTRA_LANGUAGE_OR_LOCALE, "ES")
            startActivityForResult(scanIntent, ConstantsHelper.cardScanCode)
        }
    }

    private fun load() {
        (presenter as CustomerPresenter).getCustomers({ customers: MutableList<Customer> ->

            customerList = customers
            if (customers.isEmpty() && Helper.isInternetAvailable(context!!)) {
                (presenter as CustomerPresenter).getCustomersFromServer({
                    fillList(it)
                }, { error: Throwable ->
                    UIHelper.showMessage(context!!, "Error cargando Clientes", error.message!!)
                })
            } else
                fillList(customers)
        }, { error: Throwable ->
            UIHelper.showMessage(context!!, "Error cargando Clientes", error.message!!)
        })
    }

    private fun fillList(customers: MutableList<Customer>) {
        customerList = customers
        toggleList(customerList, customerRVList)

        customerRVList.setHasFixedSize(false)
        customerRVList.layoutManager = LinearLayoutManager(context)
        customerRVList.adapter = adapter
    }

    override fun onCustomerAdded(customer: Customer) {
        adapter.add(customer)
    }

    fun setValuesFromOCR(value: String) {
        fragment.setValuesFromOCR(value)
    }

    fun searchByCreditCard(value: String) {
        searchCustomer.setText(value)
    }

    override fun onScanComple(barcode: Result?) {
        val barcodeStr = barcode?.contents!!
        searchCustomer.setText(barcodeStr.substring(1, barcodeStr.length - 1))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            ConstantsHelper.cameraCode -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    UIHelper.showMessage(context!!, "Error",
                            getString(R.string.not_file_permission_granted))
                else
                    stackFragmentToTop(BarcodeReaderFragment.newInstance(this), R.id.mainContainer,
                            false)

            }
        }
    }
}