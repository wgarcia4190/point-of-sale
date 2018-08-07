package com.devcorerd.pos.view.viewholder

import android.content.Context
import android.view.View
import android.widget.TextView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.ViewHolder
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.TransactionDetails

/**
 * @author Ing. Wilson Garcia
 * Created on 8/2/18
 */
class TransactionDetailsProductViewHolder(view: View) : ViewHolder<TransactionDetails>(view) {

    private val productName: TextView by lazy {
        view.findViewById<TextView>(R.id.productName)
    }

    private val total: TextView by lazy {
        view.findViewById<TextView>(R.id.total)
    }

    private val amount: TextView by lazy {
        view.findViewById<TextView>(R.id.amount)
    }

    override fun bindElement(entity: TransactionDetails, context: Context, listener: OnClickListener<TransactionDetails>?) {
        productName.text = entity.productName
        total.text = String.format("%.2f", (entity.productPrice * entity.quantity))
        amount.text = entity.quantity.toString().plus(" x ")
                .plus(String.format("%.2f", entity.productPrice))
    }

}