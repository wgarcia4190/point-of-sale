package com.devcorerd.pos.model.entity

/**
 * @author Ing. Wilson Garcia
 * Created on 8/2/18
 */
data class TransactionDetails(val transactionId: Int, val productName: String,
                              val productSku: String, val productPrice: Double, val quantity: Int)