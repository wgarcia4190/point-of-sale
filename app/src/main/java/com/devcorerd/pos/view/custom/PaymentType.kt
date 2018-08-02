package com.devcorerd.pos.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.devcorerd.pos.R

/**
 * @author Ing. Wilson Garcia
 * Created on 8/1/18
 */
class PaymentType(context: Context, attrSet: AttributeSet?, defStyleInt: Int) :
        FrameLayout(context, attrSet, defStyleInt) {

    constructor(context: Context, attrSet: AttributeSet?) : this(context, attrSet, 0)
    constructor(context: Context) : this(context, null, 0)

    private var view = LayoutInflater.from(context).inflate(R.layout.payment_type,
            this, true)

    private val paymentName: TextView by lazy { view.findViewById<TextView>(R.id.paymentName) }
    private val paymentContainer: LinearLayout by lazy {
        view.findViewById<LinearLayout>(R.id.paymentContainer)
    }

    init {
        init(context, attrSet!!)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        layoutParams = createLayoutParam()

        val paymentNameStr: String
        val typedArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.PaymentType,
                0, 0)

        try {
            paymentNameStr = typedArray.getString(R.styleable.PaymentType_payment)
        } finally {
            typedArray.recycle()
        }

        paymentName.text = paymentNameStr
    }

    private fun createLayoutParam(): FrameLayout.LayoutParams {
        return LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT)
    }

    override fun setOnClickListener(clickListener: OnClickListener){
        paymentContainer.setOnClickListener(clickListener)
    }

}