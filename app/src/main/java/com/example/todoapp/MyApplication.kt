package com.example.todoapp

import android.app.Application
import android.content.Context

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private var instance: MyApplication? = null
        fun getAppContext(): Context {
            return instance!!.applicationContext
        }
    }
}

