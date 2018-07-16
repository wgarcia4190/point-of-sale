package com.devcorerd.pos.listener

/**
 * Created by wgarcia on 7/16/2018.
 */
interface OnClickListener<T> {
    fun onClick(entity: T? = null)
}