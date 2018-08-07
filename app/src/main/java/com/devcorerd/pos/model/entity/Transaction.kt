package com.devcorerd.pos.model.entity

import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Ing. Wilson Garcia
 * Created on 8/2/18
 */
data class Transaction(var id: Int, val cashier: String, val paymentMethod: String, val total: Double,
                  val amount: Double, val voucher: String, val customer: String, val creationDate: DateTime,
                  var modificationDate: DateTime,
                       val transactionDetails: MutableList<TransactionDetails>? = null){
    companion object {
        fun getID(): Int{
            return Integer.parseInt(SimpleDateFormat("ddHHmmss", Locale.US).format(Date()))
        }
    }
}