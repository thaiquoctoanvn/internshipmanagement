package com.example.internshipmanagement.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.ui.platform.ViewAmbient
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.UserProfile
import com.example.internshipmanagement.ui.base.BaseActivity
import com.example.internshipmanagement.util.SERVER_URL
import kotlinx.android.synthetic.main.activity_user_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserProfileActivity : BaseActivity() {

    private val userViewModel by viewModel<UserViewModel>()

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_user_profile
    }

    override fun setViewOnEventListener() {
        ibUserProfileBack.setOnClickListener { this.finish() }
        tvProfileEvaluation.setOnClickListener { switchToEvaluationScreen() }
    }

    override fun setObserver() {
        userViewModel.getUserProfileValue().observe(this, Observer {
            updateUserProfileUI(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = intent
        if(data != null) {
            data.getStringExtra("userId")?.let { userViewModel.getUserProfile(it) }
        }
    }

    private fun updateUserProfileUI(userProfile: UserProfile) {
        if(!isMentorAccount(userProfile.type)) {
            ivIconEvaluation.visibility = View.VISIBLE
            tvProfileEvaluation.visibility = View.VISIBLE
            tvUserProfileStatus.text = getString(R.string.mentee)
        } else {
            tvUserProfileStatus.text = getString(R.string.mentor)
        }
//        if(
//            isMentorAccount(userProfile.type) ||
//            !isMentorAccount(userViewModel.getSharedPref().getString("type", "")!!)
//        ) {
//            ivIconEvaluation.visibility = View.GONE
//            tvProfileEvaluation.visibility = View.GONE
//            tvUserProfileStatus.text = getString(R.string.mentor)
//        }
        tvProfileName.text = userProfile.name
        tvNickName.text = "@" + userProfile.nickName
        tvProfilePosition.text = userProfile.role
        tvProfileEmail.text = userProfile.email
        Glide.with(this)
            .load("$SERVER_URL${userProfile.avatarUrl}")
            .circleCrop()
            .placeholder(R.drawable.default_avatar)
            .into(ivAvatarProfile)
    }

    private fun switchToEvaluationScreen() {
        val userId = userViewModel.getUserProfileValue().value?.userId
        val myId = userViewModel.getSharedPref().getString("userId", "")
        val intent = Intent(this, EvaluationProfileActivity::class.java)
        intent.apply {
            putExtra("userId", userId)
            putExtra("myId", myId)
        }
        startActivity(intent)
    }
}