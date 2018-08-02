package com.devcorerd.pos.listener

import com.devcorerd.pos.model.entity.Printer

/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
interface OnPrinterUpdated {
    fun onPrinterUpdated(position: Int, printer: Printer)
}