package com.devcorerd.pos.model.entity

/**
 * @author Ing. Wilson Garcia
 * Created on 8/2/18
 */
data class Section<T>(val title: String,  val data: MutableList<T>)