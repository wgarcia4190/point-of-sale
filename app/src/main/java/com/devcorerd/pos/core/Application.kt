package com.devcorerd.pos.core

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.devcorerd.pos.R
import com.devcorerd.pos.helper.PreferencesHelper
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker
import net.danlew.android.joda.JodaTimeAndroid

/**
 * Created by wgarcia on 7/16/2018.
 */
class Application: MultiDexApplication(){

    private var tracker: Tracker? = null
    private var analytics: GoogleAnalytics? = null

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        JodaTimeAndroid.init(this)
        PreferencesHelper.instance.createPreferences(this)
        analytics = GoogleAnalytics.getInstance(this)

        instance = this
    }

    companion object {
        @JvmField
        var instance: Application? = null

        fun get(): Application {
            return instance!!
        }
    }

    @Synchronized
    fun getDefaultTracker(): Tracker {
        if (tracker == null) {
            tracker = analytics!!.newTracker(getString(R.string.xml_tracking_id))
            tracker!!.enableAutoActivityTracking(false)
            tracker!!.enableExceptionReporting(true)

        }
        return tracker!!
    }

    fun Double.format(digits: Int) = String.format("%.${digits}f", this)
}