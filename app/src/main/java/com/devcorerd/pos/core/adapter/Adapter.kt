package com.devcorerd.pos.core.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.devcorerd.pos.listener.OnClickListener

/**
 * Created by wgarcia on 7/16/2018.
 */
class Adapter<T, V>(var data: MutableList<T>? = mutableListOf(), var context: Context,
                    private var viewHolderFactory: () -> V,
                    private var listener: OnClickListener<T>? = null) :
        RecyclerView.Adapter<V>() where V : ViewHolder<T> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V = viewHolderFactory()

    override fun getItemCount(): Int {
        return this.data!!.size
    }

    override fun onBindViewHolder(holder: V, position: Int) {
        holder.bindElement(data!![position], context, listener)
    }
}