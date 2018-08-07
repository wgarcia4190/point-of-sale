package com.devcorerd.pos.view.viewholder

import android.view.View
import android.widget.TextView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.HeaderSectionViewHolder
import com.devcorerd.pos.model.entity.Section
import com.devcorerd.pos.model.entity.Transaction

/**
 * @author Ing. Wilson Garcia
 * Created on 8/2/18
 */
class TransactionHeaderViewHolder(view: View): HeaderSectionViewHolder(view){

    private val date: TextView by lazy {
        view.findViewById<TextView>(R.id.date)
    }

    private val count: TextView by lazy {
        view.findViewById<TextView>(R.id.count)
    }

    fun bindData(section: Section<Transaction>){
        date.text = section.title

        if(section.data.size < 2)
            count.text = section.data.size.toString().plus(" Recibo")
        else
            count.text = section.data.size.toString().plus(" Recibos")
    }
}