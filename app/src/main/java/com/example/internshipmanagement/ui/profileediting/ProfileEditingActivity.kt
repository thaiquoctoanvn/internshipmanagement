package com.example.internshipmanagement.ui.profileediting

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.UserProfile
import com.example.internshipmanagement.ui.base.BaseActivity
import com.example.internshipmanagement.util.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_profile_editing.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileEditingActivity : BaseActivity() {

    private val profileEditingViewModel by viewModel<ProfileEditingViewModel>()

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

    @RequiresApi(Build.VERSION_CODES.M)
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
        permission.entries.forEach {
            when(it.key) {
                CAMERA_PERMISSION -> {
                    if(it.value) {
                        Log.d("###", "camera pass")
                        startCamera()
                    } else {
                        val showRationale = shouldShowRequestPermissionRationale(CAMERA_PERMISSION)
                        Log.d("###", "rationale: $showRationale")
                        showRationaleDialog(showRationale)
                    }
                }
                READ_STORAGE_PERMISSION -> {
                    if(it.value) {
                        Log.d("###", "gallery pass")
                        openGallery()
                    } else {
                        val showRationale = shouldShowRequestPermissionRationale(READ_STORAGE_PERMISSION)
                        Log.d("###", "rationale: $showRationale")
                        showRationaleDialog(showRationale)
                    }
                }
            }
        }
    }

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_profile_editing
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun setViewOnEventListener() {
        ibEditProfileCancel.setOnClickListener { finish() }
        tvEditProfileSave.setOnClickListener { updateUserInfo() }
        ibChangeAvatar.setOnClickListener { takeImageFrom() }
    }

    override fun setObserver() {
        profileEditingViewModel.profileInfo.observe(this, Observer {
            updateUI(it)
        })
        profileEditingViewModel.isSuccessful.observe(this, Observer {
            completeProfileEdition(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setBaseObserver(profileEditingViewModel)
        loadCurrentProfileInfo()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissions(permission: String) {
        if(ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            when(permission) {
                CAMERA_PERMISSION -> startCamera()
                READ_STORAGE_PERMISSION, WRITE_TO_STORAGE_PERMISSION -> openGallery()
            }
        } else {
            requestPermissionLauncher.launch(arrayOf(permission))
        }
    }

    private fun showRationaleDialog(isNeverShowAgain: Boolean) {
        if(!isNeverShowAgain) {
            AlertDialog.Builder(this).create().apply {
                setTitle(getString(R.string.permission_title_message))
                setMessage(getString(R.string.permission_message))
                setButton(
                    AlertDialog.BUTTON_POSITIVE,
                    "OK",
                    DialogInterface.OnClickListener() { dialogInterface: DialogInterface, which: Int ->
                    dismiss()
                })
            }.show()
        }
    }

    private fun loadCurrentProfileInfo() {
        profileEditingViewModel.getProfileInfo()
    }

    private fun updateUI(userProfile: UserProfile) {
        Glide.with(this)
            .load("$SERVER_URL${userProfile.avatarUrl}")
            .circleCrop()
            .placeholder(R.drawable.default_avatar)
            .into(ivAvatarEditProfile)
        etNameEditProfile.setText(userProfile.name)
        etPositionEditProfile.setText(userProfile.role)
        etEmailEditProfile.setText(userProfile.email)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun takeImageFrom() {

        val view = LayoutInflater.from(this).inflate(R.layout.item_pick_image_option, null)
        BottomSheetDialog(this).apply {
            setContentView(view)
            view.findViewById<TextView>(R.id.tvCameraOption).setOnClickListener {
                this.dismiss()
                checkPermissions(CAMERA_PERMISSION)
            }
            view.findViewById<TextView>(R.id.tvGalleryOption).setOnClickListener {
                this.dismiss()
                checkPermissions(READ_STORAGE_PERMISSION)
            }
            view.findViewById<TextView>(R.id.tvCloseOption).setOnClickListener { this.dismiss() }
            show()
        }
    }

    private fun updateAvatarFromUri(uri: Uri) {
        val sharedUserInfo = profileEditingViewModel.profileInfo.value
        Glide.with(this)
            .load(uri)
            .circleCrop()
            .into(ivAvatarEditProfile)
        if(sharedUserInfo != null) {
            profileEditingViewModel.updateAvatar(
                this,
                uri,
                sharedUserInfo.nickName,
                sharedUserInfo.avatarUrl,
                sharedUserInfo.userId
            )
        }
    }

    private fun updateAvatarFromBitmap(bitmap: Bitmap) {
        val sharedUserInfo = profileEditingViewModel.profileInfo.value
        Glide.with(this)
            .load(bitmap)
            .circleCrop()
            .into(ivAvatarEditProfile)
        if(sharedUserInfo != null) {
            profileEditingViewModel.updateAvatar(
                this,
                bitmap,
                sharedUserInfo.nickName,
                sharedUserInfo.avatarUrl,
                sharedUserInfo.userId
            )
        }
    }

    private fun startCamera() {
        startForCameraResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
    }

    private fun openGallery() {
        startForGalleryResult.launch("image/*")
    }

    private fun updateUserInfo() {
        val name = etNameEditProfile.text.toString().trim()
        val position = etPositionEditProfile.text.toString().trim()
        val email = etEmailEditProfile.text.toString().trim()
        profileEditingViewModel.updateProfileInfo(name, position, email)
    }

    private fun completeProfileEdition(isSucceed: Boolean) {
        if(isSucceed) {
            // Thông báo cập nhật qua bên PersonalFragment
            val intent = Intent(INFO_UPDATED)
            sendBroadcast(intent)
            this.finish()
        }
    }
}