package com.devcorerd.pos.helper

import android.text.Editable
import android.text.TextWatcher

/**
 * @author Ing. Wilson Garcia
 * Created on 7/23/18
 */
open class TextWatcher: TextWatcher{
    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

}