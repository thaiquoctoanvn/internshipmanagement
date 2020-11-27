package com.example.internshipmanagement.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.activity_mentee_task_detail.*
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
            validateLogin(it.toString())
            if(TextUtils.isEmpty(it.toString())) {
                ibClearAllUserName.visibility = View.GONE
            } else {
                ibClearAllUserName.visibility = View.VISIBLE
            }
        }
        etPassword.doAfterTextChanged {
            validateLogin(it.toString())
            btnLogIn.isEnabled = !TextUtils.isEmpty(it.toString())
            if(TextUtils.isEmpty(it.toString())) {
                ibClearAllPwd.visibility = View.GONE
            } else {
                ibClearAllPwd.visibility = View.VISIBLE
            }
        }
        ibClearAllUserName.setOnClickListener { etUserName.setText("") }
        ibClearAllPwd.setOnClickListener { etPassword.setText("") }
    }

    override fun setObserver() {
        userViewModel.isSuccessful.observe(this, Observer {
            logIn(it)
        })
    }

    private fun validateLogin(word: String) {
        if(TextUtils.isEmpty(word)) {
            btnLogIn.apply {
                backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(btnLogIn.context, R.color.mentee_light_color)
                )
                setTextColor(ContextCompat.getColor(btnLogIn.context, R.color.white))
                isEnabled = false
            }
        } else {
            btnLogIn.apply {
                backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(btnLogIn.context, R.color.mentee_strong_color)
                )
                setTextColor(ContextCompat.getColor(btnLogIn.context, R.color.white))
                isEnabled = false
            }
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