package com.example.internshipmanagement.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Constraints
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.DetailTaskReference
import com.example.internshipmanagement.ui.base.BaseActivity
import com.example.internshipmanagement.util.FunctionHelper
import com.example.internshipmanagement.util.SERVER_URL
import com.example.internshipmanagement.util.TASK_REVIEWING_PUSH
import kotlinx.android.synthetic.main.activity_task_reference_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

class TaskReferenceDetailActivity : BaseActivity() {

    private val mentorViewModel by viewModel<MentorViewModel>()

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_task_reference_detail
    }

    override fun setViewOnEventListener() {
        tvTaskReferenceDetailSave.setOnClickListener { reviewMenteeTask() }
        ibReferenceDetailBack.setOnClickListener { this.finish() }
    }

    override fun setObserver() {
        mentorViewModel.detailReference.observe(this, Observer {
            loadMarkLevel()
            updateDetailReferenceUI(it)
        })
        mentorViewModel.isSuccessful.observe(this, Observer {
            completeReviewing(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadDetailReference()
    }

    private fun loadDetailReference() {
        val intentData = intent
        if(intentData != null) {
            val referenceId = intent.getStringExtra("referenceId").toString()
            mentorViewModel.getDetailReference(referenceId)
        }
    }

    private fun loadMarkLevel() {
        ArrayAdapter(this, android.R.layout.simple_spinner_item, FunctionHelper.provideMarkLevel()).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spTaskReferenceMark.adapter = it
        }
    }

    private fun updateDetailReferenceUI(data: DetailTaskReference) {
        // Load các thông tin
        Log.d("###", "Mark: ${data.mark}")
        tvNoData.visibility = View.GONE
        if(data.materials.size > 0) {
            layoutMaterialContainer.visibility = View.VISIBLE

            generateThumbnails(data.materials)
        }
        val name = "${data.menteeName} (${data.menteeNickName})"
        tvReferenceDetailName.text = name
        tvDeadline.text = "Deadline: ${FunctionHelper.getDateFromTimeMilliSecond(data.deadline)}"
        tvReferenceDetailContent.text = data.content
        Glide.with(this)
            .load("$SERVER_URL${data.menteeAvatar}")
            .placeholder(R.drawable.default_avatar)
            .circleCrop()
            .into(ivReferenceDetail)

        // Nếu đã review thì return sau khi tải các thông tin
        if(data.isReviewed.toInt() == 1) {
            // Ko cho sửa đổi comment sau khi đã review
            etTaskReferenceComment.apply {
                isClickable = false
                isFocusable = false
            }
            etTaskReferenceComment.setText(data.comment)
            spTaskReferenceMark.apply {
                setSelection(data.mark.toInt(), true)
                isEnabled = false
            }
        }

        // Nếu đã hết hạn deadline hoặc đã nộp bài enable nút save
        if(System.currentTimeMillis() > data.deadline.toLong() || data.isSubmitted.toInt() == 1) {
            tvTaskReferenceDetailSave.apply {
                isEnabled = true
                setTextColor(ContextCompat.getColor(this.context, R.color.mentee_strong_color))
            }

        }

        // Nếu đã nộp bài đặt lại trạng thái là submitted
        if(data.isSubmitted.toInt() == 1) {
            tvReferenceDetailStatus.text = getString(R.string.submitted_state)
        }


    }

    private fun generateThumbnails(materials: MutableList<String>) {
        val thumbnails = FunctionHelper.generateImageMaterials(this, materials, layoutMaterialContainer)
        thumbnails.forEachIndexed { index, view ->
            view.setOnClickListener { openFullImage(index, materials) }
        }
    }

    private fun openFullImage(current: Int, materials: MutableList<String>) {
        val intent = Intent(this, FullImageActivity::class.java).apply {
            putExtra("selection", current.toString())
            putStringArrayListExtra("materials", materials as ArrayList<String>)
        }
        startActivity(intent)
    }

    private fun reviewMenteeTask() {
        val mark = spTaskReferenceMark.selectedItem.toString()
        val comment = etTaskReferenceComment.text.toString().trim()
        mentorViewModel.updateSpecificReferenceOfTask(mark, comment)
    }

    private fun completeReviewing(isSucceed: Boolean) {
        if(isSucceed) {
            val intent = Intent(TASK_REVIEWING_PUSH).apply {
                putExtra("taskId", mentorViewModel.detailReference.value?.taskId)
                putExtra("referId", mentorViewModel.detailReference.value?.referenceId)
            }
            sendBroadcast(intent)
            this.finish()
        } else {
            super.showSnackBar("We have some error, retry!")
        }
    }
}