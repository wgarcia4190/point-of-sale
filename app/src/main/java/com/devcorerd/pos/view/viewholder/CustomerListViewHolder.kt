package com.devcorerd.pos.view.viewholder

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.ViewHolder
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.Customer

/**
 * @author Ing. Wilson Garcia
 * Created on 7/23/18
 */
class CustomerListViewHolder(view: View): ViewHolder<Customer>(view){

    private val customerContainer: LinearLayout by lazy {
        view.findViewById<LinearLayout>(R.id.customerContainer)
    }

    private val image: ImageView by lazy { view.findViewById<ImageView>(R.id.customerImage) }
    private val name: TextView by lazy { view.findViewById<TextView>(R.id.name) }
    private val socialID: TextView by lazy { view.findViewById<TextView>(R.id.socialID) }
    private val email: TextView by lazy { view.findViewById<TextView>(R.id.email) }

    override fun bindElement(entity: Customer, context: Context,
                             listener: OnClickListener<Customer>?) {

        name.text = entity.getFullName()
        socialID.text = entity.socialID
        email.text = entity.email

        customerContainer.setOnClickListener {
            listener?.onClick(entity, null)
        }

    }

}