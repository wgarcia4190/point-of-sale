package com.devcorerd.pos.helper


/**
 * Created by wgarcia on 6/11/2018.
 */
abstract class AbstractBuilder<T> {
    var build: T? = null

    abstract fun createInstance(): T

    fun build(): T? {
        init()
        return this.build
    }

    fun init() {
        if (this.build == null) {
            this.build = createInstance()
        }
    }
}