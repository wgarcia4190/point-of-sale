package com.devcorerd.pos.view.custom

import android.app.AlertDialog
import android.app.Dialog
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import com.devcorerd.pos.R
import android.content.DialogInterface




/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
class BluetoothDeviceSelector : DialogFragment() {

    private lateinit var devices: MutableList<BluetoothDevice>
    private lateinit var deviceLabels: MutableList<String>
    private lateinit var onDeviceSelected: (BluetoothDevice) -> Unit
    private lateinit var onNoSelection: () -> Unit

    companion object {
        fun newInstance(devices: MutableList<BluetoothDevice>,
                        deviceLabels: MutableList<String>,
                        onDeviceSelected: (BluetoothDevice) -> Unit,
                        onNoSelection: () -> Unit): BluetoothDeviceSelector{
            val fragment = BluetoothDeviceSelector()

            fragment.devices = devices
            fragment.deviceLabels = deviceLabels
            fragment.onDeviceSelected = onDeviceSelected
            fragment.onNoSelection = onNoSelection
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity).setCancelable(true)
        if (!devices.isEmpty()) {
            builder.setTitle(R.string.title_select_bluetooth_device).setItems(deviceLabels.toTypedArray()
            ) { _, which -> onDeviceSelected(devices[which]) }
        } else {
            builder.setTitle(R.string.title_no_paired_devices).setPositiveButton(android.R.string.ok
            ) { _, _ -> onNoSelection() }
        }
        return builder.create()
    }

    override fun onCancel(dialog: android.content.DialogInterface?) {
        onNoSelection()
    }
}