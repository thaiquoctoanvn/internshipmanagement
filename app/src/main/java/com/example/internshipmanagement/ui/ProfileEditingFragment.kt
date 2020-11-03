package com.example.internshipmanagement.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.UserProfile
import com.example.internshipmanagement.util.CAMERA_PERMISSION
import com.example.internshipmanagement.util.CAMERA_REQUEST_CODE
import com.example.internshipmanagement.util.READ_STORAGE_PERMISSION
import com.example.internshipmanagement.util.WRITE_TO_STORAGE_PERMISSION
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_profile_editing.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ProfileEditingFragment : BaseFragment() {

    private val userViewModel by sharedViewModel<UserViewModel>()

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

    override fun getRootLayoutId(): Int {
        return R.layout.fragment_profile_editing
    }

    override fun setViewOnEventListener() {
        ibEditProfileCancel.setOnClickListener { cancelEditProfile() }
        tvEditProfileSave.setOnClickListener { updateUserInfo() }
        ibChangeAvatar.setOnClickListener { takeImageFrom() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadCurrentProfileInfo()
    }

    private fun loadCurrentProfileInfo() {
        userViewModel.getUserProfileValue().value?.let {
            Glide.with(this)
                .load(it.avatarUrl)
                .circleCrop()
                .centerCrop()
                .placeholder(R.drawable.default_avatar)
                .into(ivAvatarEditProfile)
            etNameEditProfile.setText(it.name)
            etPositionEditProfile.setText(it.role)
            etEmailEditProfile.setText(it.email)
        }

    }

    private fun takeImageFrom() {
        getPermissions()

        val view = LayoutInflater.from(context).inflate(R.layout.item_pick_image_option, null)
        activity?.let {
            BottomSheetDialog(it).apply {
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
    }

    private fun updateAvatarFromUri(uri: Uri) {
        val sharedUserInfo = userViewModel.getUserProfileValue().value
        Glide.with(this)
            .load(uri)
            .circleCrop()
            .into(ivAvatarEditProfile)
        activity?.let { context ->
            if(sharedUserInfo != null) {
                userViewModel.updateAvatar(
                    context,
                    uri,
                    sharedUserInfo.nickName,
                    sharedUserInfo.avatarUrl,
                    sharedUserInfo.userId
                )
            }
        }
    }

    private fun updateAvatarFromBitmap(bitmap: Bitmap) {
        val sharedUserInfo = userViewModel.getUserProfileValue().value
        Glide.with(this)
            .load(bitmap)
            .circleCrop()
            .into(ivAvatarEditProfile)
        activity?.let { context ->
            if(sharedUserInfo != null) {
                userViewModel.updateAvatar(
                    context,
                    bitmap,
                    sharedUserInfo.nickName,
                    sharedUserInfo.avatarUrl,
                    sharedUserInfo.userId
                )
            }
        }
    }

    private fun getPermissions() {
        requestPermissions(
            arrayOf(
                WRITE_TO_STORAGE_PERMISSION,
                CAMERA_PERMISSION,
                READ_STORAGE_PERMISSION
            ), CAMERA_REQUEST_CODE)
    }

    private fun cancelEditProfile() {
        activity?.let {
            it.supportFragmentManager.beginTransaction().detach(this).commit()
        }
    }

    private fun updateUserInfo() {
        val name = etNameEditProfile.text.toString().trim()
        val position = etPositionEditProfile.text.toString().trim()
        val email = etEmailEditProfile.text.toString().trim()
        userViewModel.updateUserInfo(name, position, email)
        cancelEditProfile()
    }

}