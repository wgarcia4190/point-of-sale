package com.devcorerd.pos.view.custom

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.widget.ProgressBar
import com.devcorerd.pos.R

/**
 * @author Ing. Wilson Garcia
 * Created on 6/3/18
 */
class DelayedProgressDialog : DialogFragment() {

    private var mProgressBar: ProgressBar? = null
    private var startedShowing: Boolean = false
    private var mStartMillisecond: Long = 0
    private var mStopMillisecond: Long = 0

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        val inflater = activity!!.layoutInflater

        builder.setView(inflater.inflate(R.layout.progress_dialog, null))
        return builder.create()
    }

    override fun onStart() {
        super.onStart()
        mProgressBar = dialog.findViewById(R.id.progress)

        if (dialog.window != null) {
            val px = (PROGRESS_CONTENT_SIZE_DP * resources.displayMetrics.density).toInt()
            dialog.window!!.setLayout(px, px)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        isCancelable = false
    }

    override fun show(fm: FragmentManager, tag: String) {
        mStartMillisecond = System.currentTimeMillis()
        startedShowing = false
        mStopMillisecond = java.lang.Long.MAX_VALUE

        val handler = Handler()
        handler.postDelayed({
            if (mStopMillisecond > System.currentTimeMillis())
                showDialogAfterDelay(fm, tag)
        }, DELAY_MILLISECOND.toLong())
    }

    private fun showDialogAfterDelay(fm: FragmentManager, tag: String) {
        startedShowing = true
        val ft = fm.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    fun cancel() {
        mStopMillisecond = System.currentTimeMillis()

        if (startedShowing) {
            if (mProgressBar != null) {
                cancelWhenShowing()
            } else {
                cancelWhenNotShowing()
            }
        }
    }

    private fun cancelWhenShowing() {
        activity?.runOnUiThread {
            try {
                if (mStopMillisecond < mStartMillisecond + DELAY_MILLISECOND.toLong() + SHOW_MIN_MILLISECOND.toLong()) {
                    val handler = Handler()
                    handler.postDelayed({ dismissAllowingStateLoss() }, SHOW_MIN_MILLISECOND.toLong())
                } else {
                    dismissAllowingStateLoss()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun cancelWhenNotShowing() {
        try {
            val handler = Handler()
            handler.postDelayed({ dismissAllowingStateLoss() }, DELAY_MILLISECOND.toLong())
        } catch (ex: Exception) {
            dismissAllowingStateLoss()
        }
    }

    companion object {
        private val DELAY_MILLISECOND = 450
        private val SHOW_MIN_MILLISECOND = 300
        private val PROGRESS_CONTENT_SIZE_DP = 80
    }
}