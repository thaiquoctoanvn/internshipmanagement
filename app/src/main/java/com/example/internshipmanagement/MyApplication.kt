package com.example.internshipmanagement

import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.internshipmanagement.di.apiModule
import com.example.internshipmanagement.di.mentorModule
import com.example.internshipmanagement.di.sharedPrefModule
import com.example.internshipmanagement.di.userModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(sharedPrefModule, userModule, apiModule, mentorModule))
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun moveToAppOnForeground() = Log.d("###", "App is on foreground")

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun moveToAppOnBackground() = Log.d("###", "App is on background")
}