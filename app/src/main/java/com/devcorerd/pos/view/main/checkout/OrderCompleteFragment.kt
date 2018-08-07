package com.devcorerd.pos.view.main.checkout

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.devcorerd.pos.BuildConfig
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.*
import com.devcorerd.pos.model.entity.CartItem
import com.devcorerd.pos.model.entity.Printer
import com.devcorerd.pos.model.entity.Transaction
import com.devcorerd.pos.model.entity.TransactionDetails
import com.devcorerd.pos.model.enum.PaymentType
import com.devcorerd.pos.model.presenter.TransactionPresenter
import com.devcorerd.pos.view.main.customer.CustomerActivity
import kotlinx.android.synthetic.main.order_complete_fragment.*
import org.joda.time.DateTime
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors


@Suppress("DEPRECATION")
/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
class OrderCompleteFragment : FragmentBase() {
    private var total: Double = 0.0
    private var amount: Double = 0.0
    private var change: Double = 0.0
    private var paymentType = PaymentType.CASH
    private var voucher: String = ""

    private lateinit var items: MutableList<CartItem>
    private var productHtml: String = ""
    private var emailHtml: String = ""
    private var productsReceipt: String = ""

    private var printer: Printer? = null

    companion object {
        @JvmStatic
        fun newInstance(total: Double, amount: Double, paymentType: PaymentType,
                        items: MutableList<CartItem>, voucher: String):
                OrderCompleteFragment {
            val fragmentBase = OrderCompleteFragment()
            val layout: Int = R.layout.order_complete_fragment

            fragmentBase.total = total
            fragmentBase.paymentType = paymentType
            fragmentBase.amount = amount

            fragmentBase.change = amount - total
            fragmentBase.items = items
            fragmentBase.voucher = voucher
            fragmentBase.createBundle(layout, TransactionPresenter())
            return fragmentBase
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (paymentType == PaymentType.CASH)
            changeContainer.visibility = View.VISIBLE

        if ((change) > 0)
            returnText.visibility = View.VISIBLE

        changeOutText.text = context!!.resources.getString(R.string.cash_data)
                .replace("{total}", String.format("%.2f", total))
                .replace("{cash}", String.format("%.2f", amount))

        returnText.text = context!!.resources.getString(R.string.return_text)
                .replace("{change}", String.format("%.2f", change))

        email.requestFocus()
        generateProductHtml()
        generateReceiptEmail()

        setupEvents()

        if (!Helper.isInternetAvailable(context!!)) {
            email.visibility = View.GONE
            whatsappContainer.visibility = View.GONE
        }

        getPrinters()
        saveTransaction()
    }

    private fun saveTransaction(){
        val transaction = Transaction(Transaction.getID(), "181", paymentType.description, total,
                amount, voucher, /*ConstantsHelper.selectedCustomer!!.name*/"3341",
                DateTime.now(), DateTime.now(), mutableListOf())

        for(item in items){
            transaction.transactionDetails!!.add(TransactionDetails(transaction.id, item.product.name,
                    item.product.sku, item.product.price, item.quantity))
        }

        (presenter as TransactionPresenter).addTransaction(transaction, {
            if(Helper.isInternetAvailable(context!!))
                saveTransactionInServer(transaction)
        }, { error: Throwable ->
            UIHelper.showMessage(context!!, "Error creando factura", error.message!!)
        })
    }

    private fun saveTransactionInServer(transaction: Transaction) {
        (presenter as TransactionPresenter).createInvoiceHeader(transaction.customer, transaction.cashier,
                DateHelper.getDateAsString(DateTime.now()), transaction.id, {

            for (index in 0 until items.size) {
                val item = items[index]
                val final = if (index == items.size - 1) "S" else "N"
                (presenter as TransactionPresenter).createInvoiceDetail(
                        item.product.soldBy.toString(), item.product.sku, transaction.id,
                        item.product.price, item.quantity, transaction.cashier, final, {

                }, { error: Throwable ->
                    UIHelper.showMessage(context!!, "Error creando factura", error.message!!)
                })
            }

        }, { error: Throwable ->
            UIHelper.showMessage(context!!, "Error creando factura", error.message!!)
        })
    }

    private fun getPrinters() {
        (presenter as TransactionPresenter).getPrinter({
            if (it == null)
                printButtonContainer.visibility = View.GONE
            else
                printer = it
        }, { error: Throwable ->
            UIHelper.showMessage(context!!, "Error obteniendo impresora", error.message!!)
            error.printStackTrace()
        })
    }

    private fun setupEvents() {
        email.setOnTouchListener(OnTouchListener { _, event ->
            val drawableRight = 2

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= email.right - email.compoundDrawables[drawableRight].bounds.width()) {
                    sendEmail()
                    return@OnTouchListener true
                }
            }
            false
        })

        whatsapp.setOnClickListener {
            sendReceipt()
        }

        print.setOnClickListener {
            val receipt = if (paymentType == PaymentType.CASH)
                Helper.readAsset(context!!.assets, "receipt.txt")
                        .replace("{{products}}", productsReceipt)
                        .replace("{{customer_code}}", ConstantsHelper.selectedCustomer!!.socialID)
                        .replace("{{total}}", String.format("%.2f", total))
                        .replace("{{amount}}", String.format("%.2f", amount))
                        .replace("{{change}}", String.format("%.2f", change))
            else
                Helper.readAsset(context!!.assets, "receipt_card.txt")
                        .replace("{{products}}", productsReceipt)
                        .replace("{{customer_code}}", ConstantsHelper.selectedCustomer!!.socialID)
                        .replace("{{total}}", String.format("%.2f", total))
                        .replace("{{voucher}}", voucher)


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

        closeButton.setOnClickListener {
            UIHelper.startActivity(activity!!, CustomerActivity::class.java)
        }
    }

    private fun generateProductHtml() {
        for (item in items) {
            val productString = Helper.readAsset(context!!.assets, "products.html")
                    .replace("{{product}}", item.product.name.plus(" x ")
                            .plus(item.quantity.toString()))
                    .replace("{{price}}", String.format("%.2f", item.product.price))

            productHtml = productHtml.plus(productString)


            productsReceipt += item.quantity.toString().plus(" x ").plus(item.product.name)
                    .plus("\t            ").plus(String.format("%.2f", item.product.price))
                    .plus("\n\n")
        }
    }

    private fun generateReceiptEmail() {

        emailHtml = if (paymentType == PaymentType.CASH) {
            Helper.readAsset(context!!.assets, "cash_email.html")
                    .replace("{{total}}", String.format("%.2f", total))
                    .replace("{{amount}}", String.format("%.2f", amount))
                    .replace("{{change}}", String.format("%.2f", change))
                    .replace("{{products}}", productHtml)
                    .replace("{{datetime}}", DateHelper.getDateHourAsString(DateTime.now()))
        } else {
            Helper.readAsset(context!!.assets, "credit_card_email.html")
                    .replace("{{total}}", String.format("%.2f", total))
                    .replace("{{products}}", productHtml)
                    .replace("{{datetime}}", DateHelper.getDateHourAsString(DateTime.now()))
        }
    }

    private fun sendEmail() {
        EmailHelper.sendEmail(email.text.toString(), emailHtml)
    }

    private fun sendReceipt() {
        if (!Helper.checkPermissions(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE),
                    ConstantsHelper.writeReadCode)
        } else
            sendPdfToWhatsApp()
    }

    private fun sendPdfToWhatsApp() {
        UIHelper.showLoadingDialog(activity!!)
        Helper.convertHtmlToPdf(context!!, emailHtml) { file: File ->

            val fileURI = FileProvider.getUriForFile(context!!,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file)

            val share = Intent()
            share.action = Intent.ACTION_SEND
            share.type = "application/pdf"
            share.putExtra(Intent.EXTRA_STREAM, fileURI)
            share.setPackage("com.whatsapp")

            activity!!.startActivity(share)

            file.deleteOnExit()
            UIHelper.hideLoadingDialog()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            ConstantsHelper.writeReadCode -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    UIHelper.showMessage(context!!, "Error",
                            context!!.getString(R.string.not_file_permission_granted))
                else
                    sendPdfToWhatsApp()
            }

        }
    }
}