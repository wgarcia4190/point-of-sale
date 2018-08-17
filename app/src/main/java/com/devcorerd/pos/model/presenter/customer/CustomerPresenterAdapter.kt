package com.devcorerd.pos.model.presenter.customer

import android.content.Context
import com.devcorerd.pos.helper.Helper
import com.devcorerd.pos.model.presenter.CustomerPresenter

/**
 * @author Ing. Wilson Garcia
 * Created on 8/11/18
 */
class CustomerPresenterAdapter private constructor(){

    companion object {
        fun getPresenter(context: Context): CustomerPresenter{
            return CustomerPresenterFactory().getCustomerPresenter(context)
        }
    }

    private class CustomerPresenterFactory{
        fun getCustomerPresenter(context: Context): CustomerPresenter{
            return when(Helper.isInternetAvailable(context)){
                true-> {
                    var presenter: CustomerPresenter? = null
                    val dbPresenter = CustomerDBPresenter(context)
                    dbPresenter.getCustomers({
                        presenter = if(it.isEmpty()) CustomerServicePresenter(context) else dbPresenter
                    }, {
                        it.printStackTrace()
                    })

                    presenter!!
                }
                else-> CustomerDBPresenter(context)
            }
        }
    }
}