package com.devcorerd.pos.view.main.transactions

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.Adapter
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.*
import com.devcorerd.pos.model.entity.Printer
import com.devcorerd.pos.model.entity.Transaction
import com.devcorerd.pos.model.entity.TransactionDetails
import com.devcorerd.pos.model.enum.PaymentType
import com.devcorerd.pos.model.presenter.TransactionPresenter
import com.devcorerd.pos.view.viewholder.TransactionDetailsProductViewHolder
import kotlinx.android.synthetic.main.transaction_details_fragment.*
import java.util.concurrent.Executors

/**
 * @author Ing. Wilson Garcia
 * Created on 8/2/18
 */
class TransactionDetailsFragment : FragmentBase() {

    private var printer: Printer? = null
    private var productsReceipt: String = ""

    private lateinit var transaction: Transaction
    private lateinit var transactionDetails: MutableList<TransactionDetails>
    private val adapter: Adapter<TransactionDetails, TransactionDetailsProductViewHolder> by lazy {
        Adapter(transactionDetails, productListRV.context, {
            val view: View = LayoutInflater.from(productListRV.context)
                    .inflate(R.layout.transaction_details_product, productListRV, false)
            TransactionDetailsProductViewHolder(view)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(transaction: Transaction):
                TransactionDetailsFragment {
            val fragmentBase = TransactionDetailsFragment()
            val layout: Int = R.layout.transaction_details_fragment

            fragmentBase.transaction = transaction
            fragmentBase.createBundle(layout, TransactionPresenter())
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPrinters()

        if (transaction.paymentMethod == PaymentType.CREDIT_CARD.description) {
            amountContainer.visibility = View.GONE
            changeContainer.visibility = View.GONE
            voucherContainer.visibility = View.VISIBLE
        } else {
            amountContainer.visibility = View.VISIBLE
            voucherContainer.visibility = View.GONE

            if ((transaction.amount - transaction.total) <= 0)
                changeContainer.visibility = View.GONE
            else
                changeContainer.visibility = View.VISIBLE
        }

        toolbarTitle.text = "#".plus(transaction.id.toString())
        invoiceNumber.text = "Factura: #".plus(transaction.id.toString())
        cashier.text = "Vendedor: ".plus(transaction.cashier)
        paymentMethod.text = transaction.paymentMethod
        total.text = String.format("%.2f", transaction.total)
        amount.text = String.format("%.2f", transaction.amount)
        change.text = String.format("%.2f", (transaction.amount - transaction.total))
        voucher.text = transaction.voucher
        date.text = DateHelper.getDateHourAsString(transaction.creationDate)

        (presenter as TransactionPresenter).getTransactionsDetails(transaction.id, {
            transactionDetails = it

            productListRV.setHasFixedSize(false)
            productListRV.layoutManager = LinearLayoutManager(context!!)
            productListRV.adapter = adapter

            for (details in transactionDetails) {
                productsReceipt += details.quantity.toString().plus(" x ").plus(details.productName)
                        .plus("\t            ").plus(String.format("%.2f", details.productPrice))
                        .plus("\n\n")
            }
        }, { error: Throwable ->
            UIHelper.showMessage(context!!, "Error cargando detalle de transacción", error.message!!)
        })

        setupEvents()
    }

    private fun getPrinters() {
        (presenter as TransactionPresenter).getPrinter({
            if (it == null)
                printButton.visibility = View.GONE
            else
                printer = it
        }, { error: Throwable ->
            UIHelper.showMessage(context!!, "Error obteniendo impresora", error.message!!)
            error.printStackTrace()
        })
    }

    private fun setupEvents() {
        backButton.setOnClickListener {
            removeFragment()
        }
        printButton.setOnClickListener {
            val receipt = if (transaction.paymentMethod == PaymentType.CASH.description)
                Helper.readAsset(context!!.assets, "receipt.txt")
                        .replace("{{products}}", productsReceipt)
                        .replace("{{customer_code}}", ConstantsHelper.selectedCustomer!!.socialID)
                        .replace("{{total}}", String.format("%.2f", transaction.total))
                        .replace("{{amount}}", String.format("%.2f", transaction.amount))
                        .replace("{{change}}", String.format("%.2f", (transaction.amount - transaction.total)))
            else
                Helper.readAsset(context!!.assets, "receipt_card.txt")
                        .replace("{{products}}", productsReceipt)
                        .replace("{{customer_code}}", ConstantsHelper.selectedCustomer!!.socialID)
                        .replace("{{total}}", String.format("%.2f", transaction.total))
                        .replace("{{voucher}}", transaction.voucher)


            UIHelper.showLoadingDialog(activity!!)
            BluetoothHelper.findBT(activity!!, printer!!.device)
            Executors.newSingleThreadExecutor().submit {
                BluetoothHelper.openBT(activity!!, {
                    BluetoothHelper.print(context!!, receipt)
                    BluetoothHelper.closeBT()
                    UIHelper.hideLoadingDialog()
                }, {
                    UIHelper.showMessage(context!!, "Error de Impresora",
                            "Por favor asegúrece de que la impresora esta conectada y vuelva a tratar")
                    UIHelper.hideLoadingDialog()
                })
            }
        }
    }
}