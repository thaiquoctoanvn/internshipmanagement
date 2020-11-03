package com.example.internshipmanagement.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getActivityRootLayout())
        setViewOnEventListener()
    }

    abstract fun getActivityRootLayout(): Int
    abstract fun setViewOnEventListener()

    fun setBaseObserver(baseViewModel: BaseViewModel) {
        baseViewModel.getIsLoadingValue().observe(this, Observer {
            if(it) {
                Log.d("###", "Logging in")
            } else {
                Log.d("###", "Done")
            }
        })
    }

    fun showSnackBar(message: String) {
        Snackbar.make(this.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }
}