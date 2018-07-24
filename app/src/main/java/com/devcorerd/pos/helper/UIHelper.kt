package com.devcorerd.pos.helper

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.widget.EditText

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

        fun clearEditText(vararg editTexts: EditText){
            for(editText in editTexts){
                editText.setText("")
            }
        }
    }
}