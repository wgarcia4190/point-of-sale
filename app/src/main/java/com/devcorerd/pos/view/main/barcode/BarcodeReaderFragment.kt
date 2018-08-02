package com.devcorerd.pos.view.main.barcode

import android.support.v4.app.Fragment
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.devcorerd.pos.R
import com.devcorerd.pos.listener.OnScanCompleted
import android.view.KeyEvent.KEYCODE_BACK




/**
 * @author Ing. Wilson Garcia
 * Created on 7/25/18
 */
class BarcodeReaderFragment: Fragment(), ZBarScannerView.ResultHandler{

    private lateinit var scannerView: ZBarScannerView
    private lateinit var listener: OnScanCompleted

    companion object {
        @JvmStatic
        fun newInstance(listener: OnScanCompleted):
                BarcodeReaderFragment {

            val fragment = BarcodeReaderFragment()

            fragment.listener = listener
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View? {
        scannerView = ZBarScannerView(activity!!)
        scannerView.setAutoFocus(true)
        return scannerView
    }

    override fun onResume() {
        super.onResume()
        scannerView.setResultHandler(this)
        scannerView.startCamera()

        if (view == null) {
            return
        }

        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener { _, keyCode, event ->
            if(event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                activity!!.supportFragmentManager.beginTransaction().remove(this@BarcodeReaderFragment).commit()
                true
            }else
                false
        }
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    override fun handleResult(result: Result?) {
        listener.onScanComple(result)
        scannerView.stopCamera()
        activity!!.supportFragmentManager.beginTransaction().remove(this).commit()
    }






}