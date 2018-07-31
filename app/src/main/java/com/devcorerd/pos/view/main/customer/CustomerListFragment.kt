package com.devcorerd.pos.view.main.customer

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.Adapter
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.Helper
import com.devcorerd.pos.helper.TextWatcher
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.listener.OnCustomerAddedListener
import com.devcorerd.pos.listener.OnCustomerSelected
import com.devcorerd.pos.model.entity.Customer
import com.devcorerd.pos.model.presenter.CustomerPresenter
import com.devcorerd.pos.view.viewholder.CustomerListViewHolder
import kotlinx.android.synthetic.main.customer_list_fragment.*

/**
 * Created by wgarcia on 7/23/2018.
 */
class CustomerListFragment : FragmentBase(), OnCustomerAddedListener {
    private lateinit var customerList: MutableList<Customer>
    private var tempCustomerList: MutableList<Customer> = mutableListOf()

    private lateinit var listener: OnCustomerSelected

    private val adapter: Adapter<Customer, CustomerListViewHolder> by lazy {
        Adapter(customerList, customerRVList.context, {
            val view: View = LayoutInflater.from(customerRVList.context)
                    .inflate(R.layout.customer_list_item, customerRVList, false)
            CustomerListViewHolder(view)
        }, object : OnClickListener<Customer> {
            override fun onClick(entity: Customer?, `object`: Any?) {
                listener.onCustomerSelected(entity!!)
                Helper.hideKeyboard(activity!!)
                backButton.performClick()
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(listener: OnCustomerSelected):
                CustomerListFragment {
            val fragmentBase = CustomerListFragment()
            val layout: Int = R.layout.customer_list_fragment

            fragmentBase.listener = listener
            fragmentBase.createBundle(layout, CustomerPresenter())
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        load()
        setupEvents()
    }

    private fun setupEvents() {
        backButton.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction().remove(this).commit()
        }

        clearSearch.setOnClickListener {
            searchCustomer.setText("")
            it.visibility = View.GONE
            adapter.swap(customerList)
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
                        if (customer.socialID.contains(text))
                            tempCustomerList.add(customer)
                    } else if (customer.getFullName().contains(text) ||
                            customer.email.contains(text))
                        tempCustomerList.add(customer)
                }

                adapter.swap(tempCustomerList)
            }
        })

        addCustomerButton.setOnClickListener {
            stackFragmentToTop(AddCustomerFragment.newInstance(this), R.id.mainContainer, false)
        }
    }

    private fun load() {
        (presenter as CustomerPresenter).getCustomers({ customers: MutableList<Customer> ->
            customerList = customers

            customerRVList.setHasFixedSize(false)
            customerRVList.layoutManager = LinearLayoutManager(context)
            customerRVList.adapter = adapter
        }, { error: Throwable ->
            print(error.message)
        })
    }

    override fun onCustomerAdded(customer: Customer) {
        adapter.add(customer)
    }
}