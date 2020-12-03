package com.example.internshipmanagement.ui.login

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.ui.main.MainActivity
import com.example.internshipmanagement.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_log_in.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class LogInActivity : BaseActivity() {

//    private val userViewModel by viewModel<UserViewModel>()
    private val logInViewModel by viewModel<LogInViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Observer loading progress bar
        super.setBaseObserver(logInViewModel)
    }

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_log_in
    }

    override fun setViewOnEventListener() {
        btnLogIn.setOnClickListener {
            logInViewModel.logIn(etUserName.text.toString().trim(), etPassword.text.toString().trim())
        }
        etUserName.doAfterTextChanged {
            handleLogInButtonState(it.toString())
            if(TextUtils.isEmpty(it.toString())) {
                ibClearAllUserName.visibility = View.GONE
            } else {
                ibClearAllUserName.visibility = View.VISIBLE
            }
        }
        etPassword.doAfterTextChanged {
            handleLogInButtonState(it.toString())
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
        logInViewModel.isSuccessful.observe(this, Observer {
            handleLogInState(it)
        })
        logInViewModel.isInfoValid.observe(this, Observer {
            handleInfoState(it)
        })
    }

    private fun handleLogInButtonState(word: String) {
        if(word.isEmpty()) {
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
                isEnabled = true
            }
        }
    }


    private fun handleInfoState(isValid: Boolean) {
        if(!isValid) {
            super.showSnackBar(getString(R.string.log_in_alert))
        }
    }

    private fun handleLogInState(state: Boolean) {
        if(state) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            super.showSnackBar("Log in failed")
        }
    }
}