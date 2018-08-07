package com.devcorerd.pos.view.viewholder

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.ItemSectionViewHolder
import com.devcorerd.pos.helper.DateHelper
import com.devcorerd.pos.model.entity.Section
import com.devcorerd.pos.model.entity.Transaction

/**
 * @author Ing. Wilson Garcia
 * Created on 8/2/18
 */
class TransactionItemViewHolder(view: View) : ItemSectionViewHolder(view) {

    private val total: TextView by lazy {
        view.findViewById<TextView>(R.id.total)
    }

    private val paymentType: TextView by lazy {
        view.findViewById<TextView>(R.id.paymentType)
    }

    private val hour: TextView by lazy {
        view.findViewById<TextView>(R.id.hour)
    }

    private val separator: View by lazy {
        view.findViewById<View>(R.id.separator)
    }

    private val transactionContainer: LinearLayout by lazy {
        view.findViewById<LinearLayout>(R.id.transactionContainer)
    }

    fun bindData(section: Section<Transaction>, childIndex: Int, callback: (Transaction) -> Unit) {
        val transaction = section.data[childIndex]
        total.text = String.format("%.2f", transaction.total)
        paymentType.text = transaction.paymentMethod
        hour.text = DateHelper.getHourString(transaction.creationDate)

        if (childIndex == section.data.size - 1)
            separator.visibility = View.GONE
        else
            separator.visibility = View.VISIBLE

        transactionContainer.setOnClickListener {
            callback(transaction)
        }
    }

}