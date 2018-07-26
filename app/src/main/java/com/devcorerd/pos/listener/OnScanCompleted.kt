package com.devcorerd.pos.listener

import me.dm7.barcodescanner.zbar.Result

/**
 * @author Ing. Wilson Garcia
 * Created on 7/25/18
 */
interface OnScanCompleted{
    fun onScanComple(barcode: Result?)
}