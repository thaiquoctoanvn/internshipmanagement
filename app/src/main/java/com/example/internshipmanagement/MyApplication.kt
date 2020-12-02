package com.example.internshipmanagement

import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.internshipmanagement.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(listOf(sharedPrefModule, userModule, apiModule, mentorModule, repositoryModule, viewModelModule))
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun moveToAppOnForeground() = Log.d("###", "App is on foreground")

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun moveToAppOnBackground() = Log.d("###", "App is on background")
}