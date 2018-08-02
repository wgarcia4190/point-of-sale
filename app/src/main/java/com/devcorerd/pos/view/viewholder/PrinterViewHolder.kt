package com.devcorerd.pos.view.viewholder

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.ViewHolder
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.model.entity.Printer

/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
class PrinterViewHolder(view: View) : ViewHolder<Printer>(view) {

    private val printerName: TextView by lazy {
        view.findViewById<TextView>(R.id.printerName)
    }

    private val printerContainer: LinearLayout by lazy {
        view.findViewById<LinearLayout>(R.id.printerContainer)
    }

    override fun bindElement(entity: Printer, context: Context, listener: OnClickListener<Printer>?) {
        printerName.text = entity.name

        printerContainer.setOnClickListener {
            listener?.onClick(entity, layoutPosition)
        }
    }

}