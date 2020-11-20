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
import kotlinx.android.synthetic.main.activity_task_reference_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.ArrayList

class TaskReferenceDetailActivity : BaseActivity() {

    private val mentorViewModel by viewModel<MentorViewModel>()

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_task_reference_detail
    }

    override fun setViewOnEventListener() {
        tvTaskReferenceDetailSave.setOnClickListener {

        }
        ibReferenceDetailBack.setOnClickListener { this.finish() }
    }

    override fun setObserver() {
        mentorViewModel.getDetailReferenceValue().observe(this, Observer {
            updateDetailReferenceUI(it)
            loadMarkLevel()
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
        tvNoData.visibility = View.GONE
        if(data.materials.size > 0) {
            layoutMaterialContainer.visibility = View.VISIBLE
            generateImageMaterials(data.materials)
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

    private fun generateImageMaterials(materials: MutableList<String>) {
        val number = materials.size
        if(number > 0) {
            val widthScreen = resources.displayMetrics.widthPixels
            val set = ConstraintSet()
            when(number) {
                0 -> return
                1 -> {
                    val image = ImageView(this).apply {
                        layoutParams = ConstraintLayout.LayoutParams(Constraints.LayoutParams.MATCH_PARENT, widthScreen / 2)
                        id = View.generateViewId()
                        scaleType = ImageView.ScaleType.CENTER_CROP

                    }
                    Glide.with(this)
                        .load(materials[0])
                        .placeholder(R.drawable.app_logo)
                        .into(image)

                    layoutMaterialContainer.addView(image)
                    set.clone(layoutMaterialContainer)

                    set.connect(image.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                    set.connect(image.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)

                    image.setOnClickListener { openFullImage(0, materials) }
                }
                2 -> {
                    val firstImage = ImageView(this).apply {
                        layoutParams = ConstraintLayout.LayoutParams(widthScreen / 2, widthScreen / 2)
                        id = View.generateViewId()
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                    val secondImage = ImageView(this).apply {
                        layoutParams = ConstraintLayout.LayoutParams(widthScreen / 2, widthScreen / 2)
                        id = View.generateViewId()
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                    Glide.with(this)
                        .load(materials[0])
                        .placeholder(R.drawable.app_logo)
                        .into(firstImage)
                    Glide.with(this)
                        .load(materials[1])
                        .placeholder(R.drawable.app_logo)
                        .into(secondImage)
                    layoutMaterialContainer.apply {
                        addView(firstImage)
                        addView(secondImage)
                    }
                    set.clone(layoutMaterialContainer)
                    set.connect(firstImage.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                    set.connect(secondImage.id, ConstraintSet.TOP, firstImage.id, ConstraintSet.TOP)
                    set.createHorizontalChain(
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.LEFT,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.RIGHT,
                        arrayOf(firstImage.id, secondImage.id).toIntArray(),
                        null,
                        ConstraintSet.CHAIN_SPREAD
                    )

                    firstImage.setOnClickListener { openFullImage(0, materials) }
                    secondImage.setOnClickListener { openFullImage(1, materials) }
                }
                else -> {

                    val firstImage = ImageView(this).apply {
                        layoutParams = ConstraintLayout.LayoutParams((widthScreen * 0.6).toInt(), widthScreen / 2)
                        id = View.generateViewId()
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                    val secondImage = ImageView(this).apply {
                        layoutParams = ConstraintLayout.LayoutParams(0, widthScreen / 2 / 2)
                        id = View.generateViewId()
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                    val thirdImage = ImageView(this).apply {
                        layoutParams = ConstraintLayout.LayoutParams(0, widthScreen / 2 / 2)
                        id = View.generateViewId()
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                    Glide.with(this)
                        .load(materials[0])
                        .placeholder(R.drawable.app_logo)
                        .into(firstImage)
                    Glide.with(this)
                        .load(materials[1])
                        .placeholder(R.drawable.app_logo)
                        .into(secondImage)
                    Glide.with(this)
                        .load(materials[2])
                        .placeholder(R.drawable.app_logo)
                        .into(thirdImage)

                    layoutMaterialContainer.apply {
                        addView(firstImage)
                        addView(secondImage)
                        addView(thirdImage)
                    }

                    set.clone(layoutMaterialContainer)
                    set.connect(firstImage.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                    set.connect(firstImage.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)

                    set.connect(secondImage.id, ConstraintSet.START, firstImage.id, ConstraintSet.END)
                    set.connect(secondImage.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

                    set.connect(thirdImage.id, ConstraintSet.START, secondImage.id, ConstraintSet.START)
                    set.connect(thirdImage.id, ConstraintSet.END, secondImage.id, ConstraintSet.END)

                    set.createVerticalChain(
                        firstImage.id,
                        ConstraintSet.TOP,
                        firstImage.id,
                        ConstraintSet.BOTTOM,
                        arrayOf(secondImage.id, thirdImage.id).toIntArray(),
                        null,
                        ConstraintSet.CHAIN_SPREAD
                    )
                    if(number > 3) {
                        val viewOpacity = TextView(this).apply {
                            layoutParams = ConstraintLayout.LayoutParams(0, 0)
                            typeface = Typeface.DEFAULT_BOLD
                            text = "+${materials.size - 3}"
                            gravity = Gravity.CENTER
                            setBackgroundResource(R.color.black_opacity)
                            setTextColor(ContextCompat.getColor(this.context, R.color.white))
                            id = View.generateViewId()
                        }
                        layoutMaterialContainer.addView(viewOpacity)

                        set.connect(viewOpacity.id, ConstraintSet.START, thirdImage.id, ConstraintSet.START)
                        set.connect(viewOpacity.id, ConstraintSet.END, thirdImage.id, ConstraintSet.END)
                        set.connect(viewOpacity.id, ConstraintSet.TOP, thirdImage.id, ConstraintSet.TOP)
                        set.connect(viewOpacity.id, ConstraintSet.BOTTOM, thirdImage.id, ConstraintSet.BOTTOM)
                        viewOpacity.setOnClickListener { openFullImage(2, materials) }
                    }
                    firstImage.setOnClickListener { openFullImage(0, materials) }
                    secondImage.setOnClickListener { openFullImage(1, materials) }
                    thirdImage.setOnClickListener { openFullImage(2, materials) }
                }
            }
            set.applyTo(layoutMaterialContainer)
        }
    }

    private fun openFullImage(current: Int, materials: MutableList<String>) {
        val intent = Intent(this, FullImageActivity::class.java).apply {
            putExtra("selection", current.toString())
            putStringArrayListExtra("materials", materials as ArrayList<String>)
        }
        startActivity(intent)
    }
}