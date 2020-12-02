package com.example.internshipmanagement.ui.applaunching

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.ui.MainActivity
import com.example.internshipmanagement.ui.UserViewModel
import com.example.internshipmanagement.ui.base.BaseActivity
import com.example.internshipmanagement.ui.login.LogInActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LaunchActivity : BaseActivity() {

//    private val userViewModel by viewModel<UserViewModel>()
    private val launchingViewModel by viewModel<LaunchingViewModel>()

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_launch
    }

    override fun setViewOnEventListener() {
    }

    override fun setObserver() {
        launchingViewModel.isSuccessful.observe(this, Observer {
            startApp(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkToken()
    }

    private fun checkToken() {
        launchingViewModel.authenticateToken()
    }

    private fun startApp(isTokenExist: Boolean) {
        if(isTokenExist) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, LogInActivity::class.java))
        }
        finish()
    }
}