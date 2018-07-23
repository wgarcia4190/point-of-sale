package com.devcorerd.pos.view.custom

import android.content.Context
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import com.devcorerd.pos.R

/**
 * @author Ing. Wilson Garcia
 * Created on 7/23/18
 */
class QuantitySelector(context: Context, attrSet: AttributeSet?, defStyleInt: Int) :
        FrameLayout(context, attrSet, defStyleInt) {

    constructor(context: Context, attrSet: AttributeSet?) : this(context, attrSet, 0)
    constructor(context: Context) : this(context, null, 0)

    private var quantity: Int = 1
    private var view = LayoutInflater.from(context).inflate(R.layout.quantity_selector,
            this, true)

    private val minusButton: ImageButton by lazy { view.findViewById<ImageButton>(R.id.minus) }
    private val plusButton: ImageButton by lazy { view.findViewById<ImageButton>(R.id.plus) }
    private val quantityText: EditText by lazy { view.findViewById<EditText>(R.id.quantity) }

    init {
        layoutParams = createLayoutParam()
        setupEvents()
        minusButton.isEnabled = false
    }

    private fun createLayoutParam(): FrameLayout.LayoutParams {
        return LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT)
    }

    private fun setupEvents() {
        minusButton.setOnClickListener {
            if (quantity > 1) {
                quantity--
                quantityText.setText(quantity.toString())

                if (quantity == 1) {
                    disableButton()
                }
            }

        }

        plusButton.setOnClickListener {
            quantity++
            quantityText.setText(quantity.toString())

            if (!minusButton.isEnabled) {
                minusButton.isEnabled = true
                minusButton.setColorFilter(ContextCompat.getColor(context, R.color.black),
                        android.graphics.PorterDuff.Mode.SRC_IN)
            }
        }
    }

    private fun disableButton() {
        minusButton.isEnabled = false
        quantity = 1
        quantityText.setText(quantity.toString())
        minusButton.setColorFilter(ContextCompat.getColor(context, R.color.deselected),
                PorterDuff.Mode.SRC_IN)
    }

    fun getQuantity(): Int {
        val tempQuantity = quantity
        quantity = 1
        disableButton()

        return tempQuantity
    }
}