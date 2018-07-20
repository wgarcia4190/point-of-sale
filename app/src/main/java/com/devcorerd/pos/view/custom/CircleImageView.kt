package com.devcorerd.pos.view.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.annotation.ColorInt
import android.util.AttributeSet
import de.hdodenhof.circleimageview.CircleImageView

/**
 * @author Ing. Wilson Garcia
 * Created on 7/19/18
 */
class CircleImageView(context: Context, attrSet: AttributeSet?, defStyleInt: Int) :
        CircleImageView(context, attrSet, defStyleInt) {

    constructor(context: Context, attrSet: AttributeSet) : this(context, attrSet, 0)
    constructor(context: Context) : this(context, null, 0)

    fun setColor(color: String){
        setImageDrawable(ColorDrawable(Color.parseColor(color)))
    }
}