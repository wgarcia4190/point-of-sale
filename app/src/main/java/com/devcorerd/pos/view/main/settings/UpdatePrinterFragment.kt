package com.devcorerd.pos.view.main.settings

import android.os.Bundle
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.BluetoothHelper
import com.devcorerd.pos.helper.Helper
import com.devcorerd.pos.helper.TextWatcher
import com.devcorerd.pos.helper.UIHelper
import com.devcorerd.pos.listener.OnPrinterDeleted
import com.devcorerd.pos.listener.OnPrinterUpdated
import com.devcorerd.pos.model.entity.Printer
import com.devcorerd.pos.model.presenter.PrinterPresenter
import kotlinx.android.synthetic.main.update_printer_fragment.*
import org.joda.time.DateTime
import java.util.concurrent.Executors

/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
class UpdatePrinterFragment : FragmentBase() {

    private lateinit var printerToUpdate: Printer
    private lateinit var updateListener: OnPrinterUpdated
    private lateinit var deleteListener: OnPrinterDeleted

    private var position = 0

    companion object {
        @JvmStatic
        fun newInstance(updateListener: OnPrinterUpdated, deleteListener: OnPrinterDeleted,
                        printer: Printer, position: Int):
                UpdatePrinterFragment {
            val fragmentBase = UpdatePrinterFragment()
            val layout: Int = R.layout.update_printer_fragment

            fragmentBase.printerToUpdate = printer
            fragmentBase.updateListener = updateListener
            fragmentBase.deleteListener = deleteListener
            fragmentBase.position = position
            fragmentBase.createBundle(layout, PrinterPresenter())
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEvents()

        printerName.setText(printerToUpdate.name)
        printerId.setText(printerToUpdate.device.plus(" (").plus(printerToUpdate.address).plus(")"))
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
                        UIHelper.showMessage(context!!, "Error de Impresora",
                                "Por favor asegúrece de que la impresora esta conectada y vuelva a tratar")
                    })
                }
            }, {
                BluetoothHelper.setDevice(null)
            })


        }

        textPrinter.setOnClickListener {
            if (BluetoothHelper.getDevice() == null) {
                UIHelper.showLoadingDialog(activity!!)

                Executors.newSingleThreadExecutor().submit {
                    BluetoothHelper.findBT(activity!!, printerToUpdate.device)
                    BluetoothHelper.openBT(activity!!, {
                        BluetoothHelper.print(context!!, Helper.readAsset(context!!.assets, "receipt.txt"))
                        UIHelper.hideLoadingDialog()
                    }, {
                        UIHelper.hideLoadingDialog()
                        UIHelper.showMessage(context!!, "Error de Impresora",
                                "Por favor asegúrece de que la impresora esta conectada y vuelva a tratar")
                        BluetoothHelper.setDevice(null)
                    })
                }
            } else
                BluetoothHelper.print(context!!, Helper.readAsset(context!!.assets, "receipt.txt"))

        }

        addPrinter.setOnClickListener {
            if (BluetoothHelper.getDevice() != null) {
                printerToUpdate.name = printerName.text.toString()
                printerToUpdate.modificationDate = DateTime.now()
                printerToUpdate.device = if (BluetoothHelper.getDevice() != null) BluetoothHelper.getDevice()!!.name else printerToUpdate.device
                printerToUpdate.address = if (BluetoothHelper.getDevice() != null) BluetoothHelper.getDevice()!!.address else printerToUpdate.address
                (presenter as PrinterPresenter).updatePrinter(printerToUpdate, {
                    updateListener.onPrinterUpdated(position, printerToUpdate)
                    backButton.performClick()
                }, { error: Throwable ->
                    UIHelper.showMessage(context!!, "Error actualizando impresoras", error.message!!)
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

        deletePrinter.setOnClickListener {
            (presenter as PrinterPresenter).deletePrinter(printerToUpdate, {
                deleteListener.onPrinterDeleted(position)
                backButton.performClick()
            }, { error: Throwable ->
                UIHelper.showMessage(context!!, "Error eliminando impresoras", error.message!!)
                error.printStackTrace()
            })
        }
    }

}