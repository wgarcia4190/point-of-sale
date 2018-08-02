package com.devcorerd.pos.view.custom

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.devcorerd.pos.R


/**
 * Created by wgarcia on 6/11/2018.
 */
@Suppress("DEPRECATION")
open class CommonDialogFragment : DialogFragment() {
    private var negativeListener: View.OnClickListener? = null
    private var positiveListener: View.OnClickListener? = null
    private var negativeButtonString: String? = null
    private var positiveButtonString: String? = null
    private var title: String? = null
    private var message: String? = null
    private var image: Int = 0
    private var callback: (() -> Unit)? = null

    private var messageTextView: TextView? = null
    private var positiveButton: Button? = null

    class DialogBuilder : BaseDialogFragmentBuilder<DialogBuilder, CommonDialogFragment>() {
        override fun createInstance(): CommonDialogFragment {
            return CommonDialogFragment()
        }
    }


    fun setPositiveListener(onClickListener: View.OnClickListener) {
        this.positiveListener = onClickListener
    }

    fun setNegativeListener(onClickListener: View.OnClickListener) {
        this.negativeListener = onClickListener
    }

    fun setCallback(callback: (() -> Unit)?) {
        this.callback = callback
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setStyle(1, 0)
    }

    override fun onCreateView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?, bundle: Bundle?): View {
        val inflate = layoutInflater.inflate(R.layout.common_dialog, viewGroup, false)
        initDialog(inflate)
        return inflate
    }


    override fun onDismiss(dialogInterface: DialogInterface?) {
        super.onDismiss(dialogInterface)
    }

    protected fun initDialog(view: View) {
        setDialogTitleView(view)
        setDialogImage(view)
        setDialogMessageView(view)
        setDialogPositiveButtonView(view)
        setDialogNegativeButtonView(view)

        if (callback != null) {
            callback!!()
        }
    }

    fun setDialogNegativeButtonView(view: View) {
        val button = view.findViewById(R.id.dialog_negative_button) as Button
        val negativeButtonText = this.negativeButtonString
        if (this.negativeListener == null || negativeButtonText == null) {
            button.visibility = View.GONE
            return
        }
        button.setOnClickListener(this.negativeListener)
        button.text = negativeButtonText
        button.transformationMethod = null
    }

    fun setDialogPositiveButtonView(view: View) {
        positiveButton = view.findViewById(R.id.dialog_positive_button) as Button
        val positiveButtonText = this.positiveButtonString
        if (this.positiveListener == null || positiveButtonText == null) {
            positiveButton!!.visibility = View.GONE
            return
        }
        positiveButton!!.text = positiveButtonText
        positiveButton!!.setOnClickListener(this.positiveListener)
        positiveButton!!.transformationMethod = null
    }

    fun setDialogMessageView(view: View) {
        messageTextView = view.findViewById(R.id.dialog_msg)
        val message = this.message
        if (message != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                messageTextView!!.text = Html.fromHtml(message.toString(), Html.FROM_HTML_MODE_LEGACY)
            } else {
                messageTextView!!.text = Html.fromHtml(message.toString())
            }
            messageTextView!!.movementMethod = LinkMovementMethod.getInstance()
            return
        }
        messageTextView!!.visibility = View.GONE
    }

    fun setDialogImage(view: View) {
        val imageView = view.findViewById(R.id.dialog_logo) as ImageView
        val drawableRes = this.image
        if (drawableRes > 0) {

            imageView.setImageResource(drawableRes)
            view.findViewById<RelativeLayout>(R.id.dialog_logo_container).visibility = View.VISIBLE
            return
        }
        view.findViewById<RelativeLayout>(R.id.dialog_logo_container).visibility = View.GONE
    }

    fun setDialogTitleView(view: View) {
        val textView = view.findViewById(R.id.dialog_title) as TextView
        val title = this.title
        if (title != null) {
            textView.text = title
        } else {
            textView.visibility = View.GONE
        }
    }

    fun setNegativeButtonString(negativeButtonString: String) {
        this.negativeButtonString = negativeButtonString
    }


    fun setPositiveButtonString(positiveButtonString: String) {
        this.positiveButtonString = positiveButtonString
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun getImage(): Int {
        return image
    }

    fun setImage(image: Int) {
        this.image = image
    }

    fun updateMessageTextView(message: String?, color: Int) {
        if (message != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                messageTextView!!.text = Html.fromHtml(message.toString(), Html.FROM_HTML_MODE_LEGACY)
            } else {
                messageTextView!!.text = Html.fromHtml(message.toString())
            }
            messageTextView!!.movementMethod = LinkMovementMethod.getInstance()

            messageTextView!!.setTextColor(ContextCompat.getColor(activity!!, color))
            return
        }
        messageTextView!!.visibility = View.GONE
    }

    fun updateMessageTextView(message: String?, buttonText: String?, color: Int) {
        if (message != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                messageTextView!!.text = Html.fromHtml(message.toString(), Html.FROM_HTML_MODE_LEGACY)
            } else {
                messageTextView!!.text = Html.fromHtml(message.toString())
            }
            messageTextView!!.movementMethod = LinkMovementMethod.getInstance()

            messageTextView!!.setTextColor(ContextCompat.getColor(activity!!, color))
            positiveButton!!.text = buttonText
            return
        }
        messageTextView!!.visibility = View.GONE
    }
}