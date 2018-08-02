package com.devcorerd.pos.core.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.view.LayoutInflater
import android.icu.lang.UCharacter.GraphemeClusterBreak.T





/**
 * @author Ing. Wilson Garcia
 * Created on 7/31/18
 */
class SpinnerAdapter<T>(context: Context?, val resource: Int, val data: MutableList<T>,
                        val createViewCallback: (position: Int, convertView: View?,
                                                 data: MutableList<T>,
                                                 callback: ((data: Any?) -> Unit)?) -> View):
        ArrayAdapter<T>(context, resource, 0, data){

    private var inflater: LayoutInflater = LayoutInflater.from(context)
    var onItemClickedCallback: ((data: Any?) -> Unit)? = null

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = inflater.inflate(resource, parent, false)
        return createViewCallback(position, view, data, onItemClickedCallback)
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = inflater.inflate(resource, parent, false)
        return createViewCallback(position, view, data, onItemClickedCallback)
    }


}