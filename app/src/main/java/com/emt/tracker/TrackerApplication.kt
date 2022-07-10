package com.emt.tracker

import android.app.Application
import android.content.Context
import com.emt.tracker.TrackerApplication.App.mContext
import io.realm.Realm

class TrackerApplication : Application() {


    object App {
        var mContext: TrackerApplication? = null
        fun getContext(): Context {
            return mContext!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        Realm.init(this)
    }


}