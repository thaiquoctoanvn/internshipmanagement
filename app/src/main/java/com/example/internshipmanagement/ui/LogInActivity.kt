package com.example.internshipmanagement.ui

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_log_in.*
import org.koin.androidx.viewmodel.ext.android.viewModel


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