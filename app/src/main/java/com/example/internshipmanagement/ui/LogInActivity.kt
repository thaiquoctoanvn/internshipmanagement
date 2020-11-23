package com.example.internshipmanagement.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_log_in.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.w3c.dom.Text


class LogInActivity : BaseActivity() {

    private val userViewModel by viewModel<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Observer loading progress bar
        super.setBaseObserver(userViewModel)
    }

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_log_in
    }

    override fun setViewOnEventListener() {
        btnLogIn.setOnClickListener {
            userViewModel.logIn(etUserName.text.toString().trim(), etPassword.text.toString().trim())
        }
        etUserName.doAfterTextChanged {
            btnLogIn.isEnabled = !TextUtils.isEmpty(it.toString())
        }
        etPassword.doAfterTextChanged {
            btnLogIn.isEnabled = !TextUtils.isEmpty(it.toString())
        }
    }

    override fun setObserver() {
        userViewModel.isSuccessful.observe(this, Observer {
            logIn(it)
        })
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