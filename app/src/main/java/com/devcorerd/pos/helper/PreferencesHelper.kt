package com.devcorerd.pos.helper

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by wgarcia on 7/16/2018.
 */
class PreferencesHelper private constructor(){

    private object Holder{
        val instance: PreferencesHelper = PreferencesHelper()
        var sharedPreferences: SharedPreferences? = null
    }

    companion object {
        val instance: PreferencesHelper by lazy { Holder.instance }
        private val db: SharedPreferences? by lazy { Holder.sharedPreferences }
    }

    fun createPreferences(context: Context){
        if(Holder.sharedPreferences == null)
            Holder.sharedPreferences = context.getSharedPreferences(
                    "pos-rd--mobile-pref-ctx", Context.MODE_PRIVATE)
    }

    fun saveString(key: String, value: String){
        val editor = db!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String {
        return if (db!!.contains(key)) {
            db!!.getString(key, "")
        } else ""
    }

    fun saveBoolean(key: String, value: Boolean){
        val editor = db!!.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String): Boolean {
        return if (db!!.contains(key)) {
            db!!.getBoolean(key, false)
        } else false
    }

}