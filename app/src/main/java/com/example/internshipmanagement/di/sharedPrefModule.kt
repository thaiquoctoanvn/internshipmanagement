package com.example.internshipmanagement.di

import android.content.Context
import com.example.internshipmanagement.util.SHARED_PREF_NAME

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharedPrefModule = module {
    fun provideSharedPref(context: Context) = context.getSharedPreferences(
        SHARED_PREF_NAME,
        Context.MODE_PRIVATE
    )

    single { provideSharedPref(androidContext()) }
}