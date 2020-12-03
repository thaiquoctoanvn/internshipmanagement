package com.example.internshipmanagement.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import kotlinx.android.synthetic.main.item_dialog_loading.*

class CustomizedLoadingDialog(private val context: Context) {
    private lateinit var dialog: Dialog

    fun showLoadingDialog() {
        if(!this::dialog.isInitialized) {
            dialog = Dialog(context).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setCanceledOnTouchOutside(false)
                setContentView(R.layout.item_dialog_loading)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
        dialog.show()
    }

    fun dismissDialog() {
        if(this::dialog.isInitialized) {
            dialog.dismiss()
        }
    }
}