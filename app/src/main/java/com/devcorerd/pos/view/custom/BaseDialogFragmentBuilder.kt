package com.devcorerd.pos.view.custom


import android.support.annotation.DrawableRes
import android.view.View
import com.devcorerd.pos.helper.AbstractBuilder


/**
 * Created by wgarcia on 6/11/2018.
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseDialogFragmentBuilder<S, T> : AbstractBuilder<T>()
        where S : BaseDialogFragmentBuilder<S, T>, T : CommonDialogFragment {

    init {
        init()
    }

    fun withPositiveListener(str: String, onClickListener: View.OnClickListener): S {
        this.build?.setPositiveButtonString(str)
        this.build?.setPositiveListener(onClickListener)
        return this as S
    }

    fun withNegativeListener(str: String, onClickListener: View.OnClickListener): S {
        this.build?.setNegativeButtonString(str)
        this.build?.setNegativeListener(onClickListener)
        return this as S
    }

    fun withTitle(str: String): S {
        this.build?.setTitle(str)
        return this as S
    }

    fun withMessage(str: String): S {
        this.build?.setMessage(str)
        return this as S
    }

    fun withImage(@DrawableRes i: Int): S {
        this.build?.setImage(i)
        return this as S
    }

    fun withCancelable(z: Boolean): S {
        this.build?.isCancelable = z
        return this as S
    }

    fun withInitCallback(z: (() -> Unit)?): S {
        this.build?.setCallback(z)
        return this as S
    }

    fun dismiss() {
        this.build?.dismiss()
    }

}