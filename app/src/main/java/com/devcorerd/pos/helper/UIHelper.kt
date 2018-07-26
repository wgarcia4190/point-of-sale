package com.devcorerd.pos.helper

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.support.v4.widget.CircularProgressDrawable
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.devcorerd.pos.R
import com.devcorerd.pos.core.ui.FragmentBase
import com.devcorerd.pos.helper.glide.GlideApp
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import kotlinx.android.synthetic.main.add_product_fragment.*
import java.io.File
import java.io.FileInputStream

/**
 * @author Ing. Wilson Garcia
 * Created on 7/16/18
 */

class UIHelper private constructor() {

    companion object {
        @JvmStatic
        fun startActivity(firstActivity: Activity, secondActivity: Class<*>) {
            val mainIntent = Intent(firstActivity, secondActivity)
            //val options = ActivityOptions.makeCustomAnimation(firstActivity, android.R.anim.slide_out_right, android.R.anim.slide_in_left)

            mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            firstActivity.startActivity(mainIntent)//, options.toBundle())
            firstActivity.finish()
        }

        fun startSubActivity(firstActivity: Activity, secondActivity: Class<*>) {
            val mainIntent = Intent(firstActivity, secondActivity)
            firstActivity.startActivity(mainIntent)
        }

        fun clearEditText(vararg editTexts: EditText){
            for(editText in editTexts){
                editText.setText("")
            }
        }

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

        fun addLoadingToImage(context: Context?, imageView: ImageView){
            val circularProgressDrawable = CircularProgressDrawable(context!!)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            imageView.setImageDrawable(circularProgressDrawable)
        }

        fun addBitmapToImage(file: File, imageView: ImageView){
            if (file.exists()) {
                val fi = FileInputStream(file)
                imageView.setImageDrawable(null)
                imageView.background = null

                imageView.setImageBitmap(BitmapFactory.decodeStream(fi))
            }
        }

        fun loadImage(context: Context, url: String, imageView: ImageView, addPlaceholder: Boolean,
                      callback: ()-> Unit) {
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
                        .listener(object: RequestListener<Drawable>{
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


    }
}