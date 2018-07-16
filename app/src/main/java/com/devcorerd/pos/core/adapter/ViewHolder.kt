package com.devcorerd.pos.core.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.devcorerd.pos.listener.OnClickListener

/**
 * Created by wgarcia on 7/16/2018.
 */
abstract class ViewHolder<T>(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    abstract fun bindElement(entity: T, context: Context, listener: OnClickListener<T>? = null): Unit
}