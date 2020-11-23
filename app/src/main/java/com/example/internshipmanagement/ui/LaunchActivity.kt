package com.example.internshipmanagement.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.ui.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LaunchActivity : BaseActivity() {

    private val userViewModel by viewModel<UserViewModel>()

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_launch
    }

    override fun setViewOnEventListener() {
    }

    override fun setObserver() {
        userViewModel.isSuccessful.observe(this, Observer {
            startApp(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBaseObserver(userViewModel)
        checkToken()
    }

    private fun checkToken() {
        userViewModel.checkToken()
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