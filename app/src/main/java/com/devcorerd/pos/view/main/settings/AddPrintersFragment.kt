package com.devcorerd.pos.view.main.settings

import android.os.Bundle
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.BluetoothHelper
import com.devcorerd.pos.helper.Helper
import com.devcorerd.pos.helper.TextWatcher
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.listener.OnAddPrinterListener
import com.devcorerd.pos.model.entity.Printer
import com.devcorerd.pos.model.presenter.PrinterPresenter
import kotlinx.android.synthetic.main.add_printer_fragment.*
import org.joda.time.DateTime
import java.util.concurrent.Executors


@Suppress("DEPRECATION")
/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
class AddPrintersFragment : FragmentBase() {

    private lateinit var listener: OnAddPrinterListener

    companion object {
        @JvmStatic
        fun newInstance(listener: OnAddPrinterListener):
                AddPrintersFragment {
            val fragmentBase = AddPrintersFragment()
            val layout: Int = R.layout.add_printer_fragment

            fragmentBase.listener = listener
            fragmentBase.createBundle(layout, PrinterPresenter())
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEvents()
    }

    private fun setupEvents() {
        UIHelper.setSpinnerEvent(dropdownSpinner, paperWidth, dropdownContainer)

        searchPrinter.setOnClickListener {
            BluetoothHelper.findBT(activity, {
                BluetoothHelper.setDevice(it)
                printerId.setText(it.name.plus(" (").plus(it.address).plus(")"))

                UIHelper.showLoadingDialog(activity!!)
                Executors.newSingleThreadExecutor().submit {
                    BluetoothHelper.openBT(activity!!, {
                        textPrinter.isEnabled = true
                        textPrinter.setTextColor(resources.getColor(R.color.white))
                        UIHelper.hideLoadingDialog()
                    }, {
                        printerId.setText("")
                        UIHelper.hideLoadingDialog()
                        UIHelper.showMessage(context!!, "Error de Impresora",
                                "Por favor asegúrece de que la impresora esta conectada y vuelva a tratar")
                    })
                }
            }, {
                UIHelper.hideLoadingDialog()
                BluetoothHelper.setDevice(null)
            })
        }

        textPrinter.setOnClickListener {
            BluetoothHelper.print(context!!, Helper.readAsset(context!!.assets, "receipt.txt"))
        }

        addPrinter.setOnClickListener {
            if (BluetoothHelper.getDevice() != null) {
                val printer = Printer(printerName.text.toString(),
                        BluetoothHelper.getDevice()!!.name,
                        BluetoothHelper.getDevice()!!.address,
                        paperWidth.text.toString(), DateTime.now(), DateTime.now())
                (presenter as PrinterPresenter).addPrinter(printer, {
                    listener.onPrinterAdded(printer)
                    backButton.performClick()
                }, { error: Throwable ->
                    UIHelper.showMessage(context!!, "Error agregando impresoras", error.message!!)
                    error.printStackTrace()
                })
            }
        }

        backButton.setOnClickListener {
            BluetoothHelper.closeBT()
            removeFragment()
        }

        printerName.addTextChangedListener(object : TextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                super.onTextChanged(s, start, before, count)

                if (count > 0) {
                    addPrinter.isEnabled = true
                    addPrinter.setTextColor(resources.getColor(R.color.white))
                } else {
                    addPrinter.isEnabled = false
                    addPrinter.setTextColor(resources.getColor(R.color.deselected))
                }
            }
        })
    }

}