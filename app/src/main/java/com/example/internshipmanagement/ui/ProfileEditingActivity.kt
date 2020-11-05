package com.example.internshipmanagement.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.UserProfile
import com.example.internshipmanagement.ui.base.BaseActivity
import com.example.internshipmanagement.util.CAMERA_PERMISSION
import com.example.internshipmanagement.util.CAMERA_REQUEST_CODE
import com.example.internshipmanagement.util.READ_STORAGE_PERMISSION
import com.example.internshipmanagement.util.WRITE_TO_STORAGE_PERMISSION
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_profile_editing.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileEditingActivity : BaseActivity() {

    private val userViewModel by viewModel<UserViewModel>()

    private val startForCameraResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK && it != null) {
            // Get intent from activity result
            val intent = it.data
            val bitmap: Bitmap = intent?.extras?.get("data") as Bitmap
            updateAvatarFromBitmap(bitmap)
        }
    }

    private val startForGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if(it != null) {
            updateAvatarFromUri(it)
        }
    }

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_profile_editing
    }

    override fun setViewOnEventListener() {
        ibEditProfileCancel.setOnClickListener { cancelEditProfile() }
        tvEditProfileSave.setOnClickListener { updateUserInfo() }
        ibChangeAvatar.setOnClickListener { takeImageFrom() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObserver()
        loadCurrentProfileInfo()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun loadCurrentProfileInfo() {
        val userId = intent.getStringExtra("userId")
        userViewModel.getUserProfile(userId!!)
    }

    private fun updateUI(userProfile: UserProfile) {
        Glide.with(this)
            .load(userProfile.avatarUrl)
            .circleCrop()
            .placeholder(R.drawable.default_avatar)
            .into(ivAvatarEditProfile)
        etNameEditProfile.setText(userProfile.name)
        etPositionEditProfile.setText(userProfile.role)
        etEmailEditProfile.setText(userProfile.email)
    }

    private fun setObserver() {
        userViewModel.getUserProfileValue().observe(this, Observer {
            updateUI(it)
        })
    }

    private fun takeImageFrom() {
        getPermissions()

        val view = LayoutInflater.from(this).inflate(R.layout.item_pick_image_option, null)
        BottomSheetDialog(this).apply {
            setContentView(view)
            view.findViewById<TextView>(R.id.tvCameraOption).setOnClickListener {
                this.dismiss()
                startForCameraResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
            }
            view.findViewById<TextView>(R.id.tvGalleryOption).setOnClickListener {
                this.dismiss()
                startForGalleryResult.launch("image/*")
            }
            view.findViewById<TextView>(R.id.tvCloseOption).setOnClickListener { this.dismiss() }
            show()
        }
    }

    private fun updateAvatarFromUri(uri: Uri) {
        val sharedUserInfo = userViewModel.getUserProfileValue().value
        Glide.with(this)
            .load(uri)
            .circleCrop()
            .into(ivAvatarEditProfile)
        if(sharedUserInfo != null) {
            userViewModel.updateAvatar(
                this,
                uri,
                sharedUserInfo.nickName,
                sharedUserInfo.avatarUrl,
                sharedUserInfo.userId
            )
        }
    }

    private fun updateAvatarFromBitmap(bitmap: Bitmap) {
        val sharedUserInfo = userViewModel.getUserProfileValue().value
        Glide.with(this)
            .load(bitmap)
            .circleCrop()
            .into(ivAvatarEditProfile)
        if(sharedUserInfo != null) {
            userViewModel.updateAvatar(
                this,
                bitmap,
                sharedUserInfo.nickName,
                sharedUserInfo.avatarUrl,
                sharedUserInfo.userId
            )
        }
    }

    private fun getPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                WRITE_TO_STORAGE_PERMISSION,
                CAMERA_PERMISSION,
                READ_STORAGE_PERMISSION
            ), CAMERA_REQUEST_CODE
        )
    }

    private fun cancelEditProfile() {
        finish()
    }

    private fun updateUserInfo() {
        val name = etNameEditProfile.text.toString().trim()
        val position = etPositionEditProfile.text.toString().trim()
        val email = etEmailEditProfile.text.toString().trim()
        userViewModel.updateUserInfo(name, position, email)
        cancelEditProfile()
    }
}