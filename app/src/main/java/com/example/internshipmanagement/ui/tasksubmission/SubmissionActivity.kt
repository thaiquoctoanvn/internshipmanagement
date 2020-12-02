package com.example.internshipmanagement.ui.tasksubmission

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import com.example.internshipmanagement.ui.base.BaseActivity
import com.example.internshipmanagement.util.CAMERA_PERMISSION
import com.example.internshipmanagement.util.READ_STORAGE_PERMISSION
import com.example.internshipmanagement.util.SUBMISSION_PUSH
import com.example.internshipmanagement.util.WRITE_TO_STORAGE_PERMISSION
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_submission.*
import kotlinx.android.synthetic.main.item_attached_image.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SubmissionActivity : BaseActivity() {

    private lateinit var attachedImageAdapter: AttachedImageAdapter

//    private val menteeViewModel by viewModel<MenteeViewModel>()
    private val taskSubmissionViewModel by viewModel<TaskSubmissionViewModel>()

    private val uriList = mutableListOf<String>()
    private var cameraBitmap: Bitmap? = null
    private var fromCamera = true
    private var referId = ""

    @RequiresApi(Build.VERSION_CODES.M)
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            permission.entries.forEach {
                when (it.key) {
                    CAMERA_PERMISSION -> {
                        if (it.value) {
                            Log.d("###", "camera pass")
                            startCamera()
                        } else {
                            val showRationale =
                                shouldShowRequestPermissionRationale(CAMERA_PERMISSION)
                            Log.d("###", "rationale: $showRationale")
                            showRationaleDialog(showRationale)
                        }
                    }
                    READ_STORAGE_PERMISSION -> {
                        if (it.value) {
                            Log.d("###", "gallery pass")
                            openGallery()
                        } else {
                            val showRationale =
                                shouldShowRequestPermissionRationale(READ_STORAGE_PERMISSION)
                            Log.d("###", "rationale: $showRationale")
                            showRationaleDialog(showRationale)
                        }
                    }
                }
            }
        }

    private val startForCameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it != null) {
                // Get intent from activity result
                val intent = it.data
                val bitmap: Bitmap = intent?.extras?.get("data") as Bitmap

                layoutCameraAttach.visibility = View.VISIBLE
                rvAttachImage.visibility = View.GONE
                if(uriList.size > 0) {
                    uriList.clear()
                }

                Glide.with(this)
                    .load(bitmap)
                    .placeholder(R.drawable.app_logo)
                    .error(R.drawable.app_logo)
                    .into(ivAttachedImage)

                cameraBitmap = bitmap
                fromCamera = true
            }
        }

    private val startForGalleryResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            if (it.resultCode == Activity.RESULT_OK && it != null) {
                val intentData = it.data
                val count = intentData?.clipData?.itemCount
                if (count != null) {
                    // Chọn nhiều hình
                    for (index in 0 until count) {
                        uriList.add(intentData.clipData!!.getItemAt(index).uri.toString())
                    }
                    updateAttachedImagesContainer(uriList)
                } else {
                    // Chọn 1 hình
                    intentData?.data?.let { uri -> uriList.add(uri.toString()) }
                    updateAttachedImagesContainer(uriList)
                }

                fromCamera = false
                layoutCameraAttach.visibility = View.GONE
                rvAttachImage.visibility = View.VISIBLE
            }
        }

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_submission
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun setViewOnEventListener() {
        ibSubmitTaskBack.setOnClickListener { this.finish() }
        tvSubmitTask.setOnClickListener { submitWork() }
        tvAttachImage.setOnClickListener { takeImageFrom() }
        ibAttachedImageRemove.setOnClickListener { removeCameraAttached() }
    }

    override fun setObserver() {
        taskSubmissionViewModel.isSuccessful.observe(this, Observer {
            completeSubmitWork(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setBaseObserver(taskSubmissionViewModel)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissions(permission: String) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            when (permission) {
                CAMERA_PERMISSION -> startCamera()
                READ_STORAGE_PERMISSION, WRITE_TO_STORAGE_PERMISSION -> openGallery()
            }
        } else {
            requestPermissionLauncher.launch(arrayOf(permission))
        }
    }

    private fun showRationaleDialog(isNeverShowAgain: Boolean) {
        if (!isNeverShowAgain) {
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

    private fun startCamera() {
        startForCameraResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.action = Intent.ACTION_OPEN_DOCUMENT
        startForGalleryResult.launch(intent)
    }

    private fun updateAttachedImagesContainer(items: MutableList<String>) {
        if (!this::attachedImageAdapter.isInitialized) {
            attachedImageAdapter = AttachedImageAdapter(onItemRemove)
        }
        attachedImageAdapter.submitList(items)
        rvAttachImage.adapter = attachedImageAdapter
    }

    private fun removeCameraAttached() {
        ivAttachedImage.setImageDrawable(null)
        layoutCameraAttach.visibility = View.GONE
        cameraBitmap = null
    }

    private fun submitWork() {
        val taskNote = etSubmissionNote.text.toString().trim()
        referId = intent.getStringExtra("referId").toString()
        if(!referId.isNullOrEmpty()) {
            if(fromCamera) {
                if(cameraBitmap == null) {
                    super.showSnackBar("You must submit your work with at least one image")
                    return
                }
                taskSubmissionViewModel.uploadMaterialsFromBitmap(this, referId, cameraBitmap!!, taskNote)
            } else {
                if(uriList.size == 0) {
                    super.showSnackBar("You must submit your work with at least one image")
                    return
                }
                taskSubmissionViewModel.uploadMaterialSFromUri(this, referId, uriList, taskNote)
            }
        } else {
            super.showSnackBar("Oops! Error, retry")
        }
    }

    private fun completeSubmitWork(flag: Boolean) {
        if(flag) {
            val intent = Intent(SUBMISSION_PUSH)
            intent.putExtra("referId", referId)
            sendBroadcast(intent)

            this.finish()
        } else {
            super.showSnackBar("Submit error, retry")
        }
    }

    private val onItemRemove: (position: Int) -> Unit = {
        uriList.removeAt(it)
        attachedImageAdapter.notifyItemRemoved(it)
    }
}