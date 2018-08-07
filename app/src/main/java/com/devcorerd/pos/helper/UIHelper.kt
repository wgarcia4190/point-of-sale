package com.devcorerd.pos.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.support.v4.app.FragmentActivity
import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatRadioButton
import android.view.View
import android.widget.*
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.devcorerd.pos.R
import com.devcorerd.pos.core.adapter.SpinnerAdapter
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.glide.GlideApp
import com.devcorerd.pos.view.custom.CommonDialogFragment
import com.devcorerd.pos.view.custom.DelayedProgressDialog
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import java.io.File
import java.io.FileInputStream

/**
 * @author Ing. Wilson Garcia
 * Created on 7/16/18
 */

@SuppressLint("StaticFieldLeak")
class UIHelper private constructor() {

    companion object {

        var selectedCategoryRadio: AppCompatRadioButton? = null
        var selectedCategoryTextView: TextView? = null
        var loadingDialog : DelayedProgressDialog = DelayedProgressDialog()

        @JvmStatic
        fun startActivity(firstActivity: Activity, secondActivity: Class<*>) {
            val mainIntent = Intent(firstActivity, secondActivity)
            //val options = ActivityOptions.makeCustomAnimation(firstActivity, android.R.anim.slide_out_right, android.R.anim.slide_in_left)

            mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            firstActivity.startActivity(mainIntent)//, options.toBundle())
            firstActivity.finish()
        }

        @JvmStatic
        fun startSubActivity(firstActivity: Activity, secondActivity: Class<*>) {
            val mainIntent = Intent(firstActivity, secondActivity)
            firstActivity.startActivity(mainIntent)
        }

        @JvmStatic
        fun startSubActivityForResult(firstActivity: Activity, secondActivity: Class<*>, requestCode: Int) {
            val mainIntent = Intent(firstActivity, secondActivity)
            firstActivity.startActivityForResult(mainIntent, requestCode)
        }

        @JvmStatic
        fun clearEditText(vararg editTexts: EditText) {
            for (editText in editTexts) {
                editText.setText("")
            }
        }

        @JvmStatic
        fun createImagePicker(context: FragmentBase): ImagePicker {
            return ImagePicker.create(context)
                    .returnMode(ReturnMode.ALL)
                    .folderMode(true)
                    .single()
                    .showCamera(true)
                    .toolbarImageTitle(context.getString(R.string.toolbar_image_title))
                    .theme(R.style.ImagePickerTheme)
                    .enableLog(true)
        }

        @JvmStatic
        fun addLoadingToImage(context: Context?, imageView: ImageView) {
            val circularProgressDrawable = CircularProgressDrawable(context!!)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            imageView.setImageDrawable(circularProgressDrawable)
        }

        @JvmStatic
        fun addBitmapToImage(file: File, imageView: ImageView) {
            if (file.exists()) {
                val fi = FileInputStream(file)
                imageView.setImageDrawable(null)
                imageView.background = null

                imageView.setImageBitmap(BitmapFactory.decodeStream(fi))
            }
        }

        @JvmStatic
        fun loadImage(context: Context, url: String, imageView: ImageView, addPlaceholder: Boolean,
                      callback: () -> Unit) {
            if (addPlaceholder) {
                val circularProgressDrawable = CircularProgressDrawable(context)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.start()

                imageView.isDrawingCacheEnabled = true
                GlideApp.with(context)
                        .load(url.trim())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(circularProgressDrawable)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?,
                                                      target: Target<Drawable>?,
                                                      isFirstResource: Boolean): Boolean {
                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?,
                                                         target: Target<Drawable>?,
                                                         dataSource: DataSource?,
                                                         isFirstResource: Boolean): Boolean {
                                callback()
                                return false
                            }

                        })
                        .into(imageView)
            } else {
                GlideApp.with(context)
                        .load(url.trim())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imageView)
            }
        }

        @JvmStatic
        fun fillSpinner(context: Context, list: MutableList<String>, spinner: Spinner) {
            val adapter: ArrayAdapter<String> = ArrayAdapter(context,
                    android.R.layout.simple_spinner_item, list)

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        @JvmStatic
        fun setSpinnerEvent(spinner: Spinner, container: View,
                            callback: (data: Any?) -> Unit) {
            container.setOnClickListener { _: View? ->
                spinner.performClick()
            }

            (spinner.adapter as SpinnerAdapter<*>).onItemClickedCallback = callback
        }

        @JvmStatic
        fun setSpinnerEvent(spinner: Spinner, textview: TextView, container: View) {
            container.setOnClickListener { _: View? ->
                spinner.performClick()
            }

            textview.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus)
                    spinner.performClick()
            }

            textview.setOnClickListener {
                spinner.performClick()
            }

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    textview.text = spinner.selectedItem.toString()
                }
            }
        }


        @JvmStatic
        fun showMessage(context: Context, title: String, message: String) {
            showAlertMessage(context as AppCompatActivity, message, title)
        }

        @JvmStatic
        fun showMessage(context: Context, message: String, title: String, callback: (() -> Unit)?) {
            showAlertMessage(context as AppCompatActivity, message, title, callback)
        }

        @JvmStatic
        fun showMessage(context: Context, message: String, title: String, callback: (() -> Unit)?, positive: String, negative: String) {
            showAlertMessage(context as AppCompatActivity, message, title,
                    positive, negative,0, callback, null)

        }

        @JvmStatic
        fun showAlertMessage(context: AppCompatActivity, message: String, title: String): CommonDialogFragment {
            return showAlertMessage(context, message, title, null)
        }

        @JvmStatic
        fun showAlertMessage(context: AppCompatActivity, message: String, title: String, callback: (() -> Unit)?): CommonDialogFragment {
            return showAlertMessage(context, message, title, "Ok", null, 0, callback, null, null)
        }

        @JvmStatic
        fun showAlertMessage(context: AppCompatActivity, message: String, title: String, buttonText: String, image: Int,
                             callback: (() -> Unit)?, initCallback: (() -> Unit)?): CommonDialogFragment {
            return showAlertMessage(context, message, title, buttonText, null, image, callback, null, initCallback)
        }

        @JvmStatic
        fun showAlertMessage(context: AppCompatActivity, message: String, title: String,
                             positiveButtonText: String, negativeButtonText: String,
                             image: Int, callbackPositive: (() -> Unit)?,
                             negaviteCallback: (() -> Unit)?): CommonDialogFragment {

            return showAlertMessage(context, message, title, positiveButtonText, negativeButtonText,
                    image, callbackPositive, negaviteCallback, null)
        }

        @JvmStatic
        fun showAlertMessage(context: AppCompatActivity, message: String, title: String,
                             positiveButtonText: String, negativeButtonText: String?,
                             image: Int, callbackPositive: (() -> Unit)?,
                             negaviteCallback: (() -> Unit)?, initCallback: (() -> Unit)?): CommonDialogFragment {
            val dialogBuilder: CommonDialogFragment.DialogBuilder = CommonDialogFragment.DialogBuilder()
            dialogBuilder.withTitle(title)
            dialogBuilder.withImage(image)
            dialogBuilder.withMessage(message)
            dialogBuilder.withPositiveListener(positiveButtonText, View.OnClickListener {
                dialogBuilder.dismiss()
                if (callbackPositive != null) {
                    callbackPositive()
                }
            })
            if (negativeButtonText != null) {
                dialogBuilder.withNegativeListener(negativeButtonText, View.OnClickListener {
                    dialogBuilder.dismiss()
                    if (negaviteCallback != null) {
                        negaviteCallback()
                    }
                })
            }
            if (initCallback != null) {
                dialogBuilder.withInitCallback(initCallback)
            }
            dialogBuilder.withCancelable(false)
            val dialogFragment: CommonDialogFragment = dialogBuilder.build()!!
            dialogFragment.show(context.supportFragmentManager, context.localClassName)

            return dialogFragment
        }

        fun showLoadingDialog(context: FragmentActivity){
            loadingDialog.show(context.supportFragmentManager, context.localClassName)
        }

        fun hideLoadingDialog(){
            loadingDialog.cancel()
        }


    }
}