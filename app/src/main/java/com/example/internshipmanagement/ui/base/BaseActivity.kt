package com.example.internshipmanagement.ui.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.util.CustomizedLoadingDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.view.*

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var loadingDialog: CustomizedLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getActivityRootLayout())
        setViewOnEventListener()
        setObserver()
        loadingDialog = CustomizedLoadingDialog(this)
    }

    abstract fun getActivityRootLayout(): Int
    abstract fun setViewOnEventListener()
    abstract fun setObserver()

    fun setBaseObserver(baseViewModel: BaseViewModel) {
        baseViewModel.isLoading.observe(this, Observer {
            if(it) {
                Log.d("###", "Logging in")
                loadingDialog.showLoadingDialog()
            } else {
                Log.d("###", "Done")
                loadingDialog.dismissDialog()
            }
        })
        baseViewModel.messageResponse.observe(this, Observer {
            showSnackBar(it)
        })
    }

    fun showSnackBar(message: String) {
        Snackbar.make(this.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    fun isMentorAccount(type: String): Boolean {
        // 1 - mentor
        // 2 - mentee
        if(type == "1") {
            return true
        }
        return false
    }
}