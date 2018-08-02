package com.devcorerd.pos.view.main.settings

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.Adapter
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.listener.OnAddPrinterListener
import com.devcorerd.pos.listener.OnClickListener
import com.devcorerd.pos.listener.OnPrinterDeleted
import com.devcorerd.pos.listener.OnPrinterUpdated
import com.devcorerd.pos.model.entity.Printer
import com.devcorerd.pos.model.presenter.PrinterPresenter
import com.devcorerd.pos.view.viewholder.PrinterViewHolder
import kotlinx.android.synthetic.main.printers_fragment.*

/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
class PrintersFragment : FragmentBase(), OnAddPrinterListener, OnPrinterUpdated, OnPrinterDeleted{
    private lateinit var printerList: MutableList<Printer>
    private val adapter: Adapter<Printer, PrinterViewHolder> by lazy {
        Adapter(printerList, printerListRV.context, {
            val view: View = LayoutInflater.from(printerListRV.context)
                    .inflate(R.layout.printer_item, printerListRV, false)
            PrinterViewHolder(view)
        }, object : OnClickListener<Printer> {
            override fun onClick(entity: Printer?, `object`: Any?) {
                val index = `object` as Int

                stackFragmentToTop(UpdatePrinterFragment.newInstance(
                        this@PrintersFragment, this@PrintersFragment,
                        entity!!, index), R.id.mainContainer, false)
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance():
                PrintersFragment {
            val fragmentBase = PrintersFragment()
            val layout: Int = R.layout.printers_fragment

            fragmentBase.createBundle(layout, PrinterPresenter())
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEvents()

        (presenter as PrinterPresenter).getPrinters({
            printerList = it

            printerListRV.setHasFixedSize(false)
            printerListRV.layoutManager = LinearLayoutManager(context!!)
            printerListRV.adapter = adapter
        }, { error: Throwable ->
            UIHelper.showMessage(context!!, "Error cargando impresoras", error.message!!)
            error.printStackTrace()
        })
    }

    private fun setupEvents() {
        addPrinter.setOnClickListener {
            stackFragmentToTop(AddPrintersFragment.newInstance(this), R.id.mainContainer,
                    false)
        }

        backButton.setOnClickListener {
            removeFragment()
        }
    }

    override fun onPrinterAdded(printer: Printer) {
        adapter.add(printer)
    }

    override fun onPrinterUpdated(position: Int, printer: Printer) {
        adapter.update(printer, position)
    }

    override fun onPrinterDeleted(position: Int) {
        adapter.delete(position)
    }
}