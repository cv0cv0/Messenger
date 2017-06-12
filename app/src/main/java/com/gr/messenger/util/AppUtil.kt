package com.gr.messenger.util

import android.app.Application

/**
 * Created by gr on 2017/6/12.
 */

class AppUtil : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
    }

    companion object {
        lateinit var application: AppUtil
    }
}