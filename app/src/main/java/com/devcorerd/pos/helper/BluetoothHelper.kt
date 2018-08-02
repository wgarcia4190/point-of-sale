package com.devcorerd.pos.helper

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import com.devcorerd.pos.view.custom.BluetoothDeviceSelector
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset
import java.util.*

/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
class BluetoothHelper private constructor() {
    companion object {

        private var bluetoothAdapter: BluetoothAdapter? = null
        private var socket: BluetoothSocket? = null
        private var device: BluetoothDevice? = null

        private var outputStream: OutputStream? = null
        private var inputStream: InputStream? = null
        private var workerThread: Thread? = null

        private var readBuffer: ByteArray? = null
        private var readBufferPosition: Int = 0
        @Volatile
        private var stopWorker: Boolean = false

        fun getDevice(): BluetoothDevice? {
            return device
        }

        fun setDevice(mDevice: BluetoothDevice?) {
            device = mDevice
        }

        fun findBT(context: FragmentActivity?, onDeviceSelected: (BluetoothDevice) -> Unit,
                   onNoSelection: () -> Unit) {

            try {
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                if (bluetoothAdapter == null) {
                    Toast.makeText(context!!, "No bluetooth adapter available", Toast.LENGTH_LONG).show()
                }

                if (!bluetoothAdapter!!.isEnabled) {
                    val enableBluetooth = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    context!!.startActivityForResult(enableBluetooth, 0)
                }

                val pairedDevices: MutableSet<BluetoothDevice> = bluetoothAdapter!!.bondedDevices
                val deviceLabels: MutableList<String> = mutableListOf()
                val devices: MutableList<BluetoothDevice> = mutableListOf()

                if (pairedDevices.size > 0) {
                    for (device in pairedDevices) {
                        deviceLabels.add(device.name)
                        devices.add(device)
                    }

                    BluetoothDeviceSelector.newInstance(devices, deviceLabels, {
                        device = it
                        onDeviceSelected(it)
                    }, {
                        device = null
                    }).show(context!!.supportFragmentManager, context.localClassName)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun findBT(context: FragmentActivity?, deviceName: String) {

            try {
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                if (bluetoothAdapter == null) {
                    Toast.makeText(context!!, "No bluetooth adapter available", Toast.LENGTH_LONG).show()
                }

                if (!bluetoothAdapter!!.isEnabled) {
                    val enableBluetooth = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    context!!.startActivityForResult(enableBluetooth, 0)
                }

                val pairedDevices: MutableSet<BluetoothDevice> = bluetoothAdapter!!.bondedDevices

                if (pairedDevices.size > 0) {
                    for (mDevice in pairedDevices) {
                        if (mDevice.name == deviceName) {
                            device = mDevice
                            break
                        }
                    }

                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        @Throws(IOException::class)
        fun openBT(activity: FragmentActivity, callback: () -> Unit, error: () -> Unit) {
            try {
                val uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
                socket = device!!.createRfcommSocketToServiceRecord(uuid)
                socket!!.connect()
                outputStream = socket!!.outputStream
                inputStream = socket!!.inputStream

                beginListenForData()

                activity.runOnUiThread {
                    callback.invoke()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                activity.runOnUiThread {
                    error.invoke()
                    closeBT()
                }

            }

        }

        @Throws(IOException::class)
        fun print(context: Context, data: String) {
            try {
                outputStream!!.write(data.toByteArray())

            } catch (e: Exception) {
                e.printStackTrace()
                UIHelper.showMessage(context, "Error de Impresora",
                        "Por favor asegúrece de que la impresora esta conectada y vuelva a tratar")
            }

        }

        @Throws(IOException::class)
        fun closeBT() {
            try {
                stopWorker = true
                outputStream!!.close()
                inputStream!!.close()
                socket!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        private fun beginListenForData() {
            try {
                val delimiter: Byte = 10

                stopWorker = false
                readBufferPosition = 0
                readBuffer = ByteArray(1024)

                workerThread = Thread(Runnable {
                    while (!Thread.currentThread().isInterrupted && !stopWorker) {
                        try {
                            val bytesAvailable = inputStream!!.available()
                            if (bytesAvailable > 0) {

                                val packetBytes = ByteArray(bytesAvailable)
                                inputStream!!.read(packetBytes)

                                for (i in 0 until bytesAvailable) {

                                    val b = packetBytes[i]
                                    if (b == delimiter) {
                                        val encodedBytes = ByteArray(readBufferPosition)
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.size
                                        )

                                        val data = String(encodedBytes, Charset.forName("US-ASCII"))
                                        readBufferPosition = 0

                                    } else {
                                        readBuffer!![readBufferPosition++] = b
                                    }
                                }
                            }

                        } catch (ex: IOException) {
                            stopWorker = true
                        }

                    }
                })

                workerThread!!.start()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }


}