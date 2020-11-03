package com.example.internshipmanagement.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit


class LogInActivity : BaseActivity() {

    private val userViewModel by viewModel<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Observer loading progress bar
        super.setBaseObserver(userViewModel)
        // Observer chuyển vào dashboard nếu đăng nhập thành công
        userViewModel.getIsSucceedValue().observe(this, Observer {
            logIn(it)
        })
        // Kiểm tra token để tự đăng nhập
        userViewModel.checkToken()
    }

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_log_in
    }

    override fun setViewOnEventListener() {
        btnLogIn.setOnClickListener {
            userViewModel.logIn(etUserName.text.toString().trim(), etPassword.text.toString().trim())
        }
    }

    private fun logIn(state: Boolean) {
        if(state) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            super.showSnackBar("Fail")
        }
    }
}