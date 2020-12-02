package com.example.internshipmanagement.ui.evaluationprofile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.MenteesEvaluation
import com.example.internshipmanagement.data.entity.UserProfile
import com.example.internshipmanagement.ui.evaluationcreating.EvaluationCreatingActivity
import com.example.internshipmanagement.ui.MentorViewModel
import com.example.internshipmanagement.ui.UserViewModel
import com.example.internshipmanagement.ui.base.BaseActivity
import com.example.internshipmanagement.util.SERVER_URL
import kotlinx.android.synthetic.main.activity_evaluation_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EvaluationProfileActivity : BaseActivity() {

    private lateinit var evaluationProfileAdapter: EvaluationProfileAdapter

//    private val userViewModel by viewModel<UserViewModel>()
//    private val mentorViewModel by viewModel<MentorViewModel>()
    private val evaluationProfileViewModel by viewModel<EvaluationProfileViewModel>()

    private var userId = ""
    private var myId = ""

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_evaluation_profile
    }

    override fun setViewOnEventListener() {
        ibAddEvaluation.setOnClickListener { switchToEvaluationCreating() }
        ibEvaluationProfileBack.setOnClickListener { this.finish() }
    }

    override fun setObserver() {
        evaluationProfileViewModel.menteeProfile.observe(this, Observer {
            updateMenteeInfoUI(it)
        })
        evaluationProfileViewModel.menteesEvaluations.observe(this, Observer {
            updateEvaluationsUI(it)
        })
        evaluationProfileViewModel.isMyMentee.observe(this, Observer {
            updateManagementStatus(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadEvaluationProfile()
    }

    override fun onRestart() {
        super.onRestart()
        evaluationProfileViewModel.getMenteesEvaluations(userId)
    }

    private fun loadEvaluationProfile() {
        val intentData = intent
        if(intentData != null) {
            userId = intentData.getStringExtra("userId").toString()
            myId = intentData.getStringExtra("myId").toString()
            if(userId.isNotEmpty() && myId.isNotEmpty()) {
                if(userId != myId ) {
                    evaluationProfileViewModel.isMyMentee(userId, myId)
                }
                evaluationProfileViewModel.getMenteesEvaluations(userId)
                evaluationProfileViewModel.getMenteeProfile(userId)
            }
        }
    }

    private fun updateMenteeInfoUI(userProfile: UserProfile) {
        Glide.with(this)
            .load("$SERVER_URL${userProfile.avatarUrl}")
            .placeholder(R.drawable.default_avatar)
            .circleCrop()
            .into(ivEvaluationProfile)
        if(userProfile.type == "1") {
            tvEvaluationProfileType.text = getString(R.string.mentor)
        }
        val name = "${userProfile.name} (${userProfile.nickName})"
        tvEvaluationProfileName.text = name
        tvEvaluationProfilePosition.text = userProfile.role
    }

    private fun updateManagementStatus(isMyMentee: Boolean) {
        tvEvaluationProfileType.visibility = View.VISIBLE
        if(isMyMentee) {
            ibAddEvaluation.visibility = View.VISIBLE
            tvEvaluationProfileType.text = getString(R.string.under_management)
        } else {
            ibAddEvaluation.visibility = View.GONE
            tvEvaluationProfileType.text = getString(R.string.have_not_managed_yet)
        }
    }

    private fun updateEvaluationsUI(evaluations: MutableList<MenteesEvaluation>) {
        if(!this::evaluationProfileAdapter.isInitialized) {
            evaluationProfileAdapter = EvaluationProfileAdapter()
        }
        if(evaluations.size > 0) {
            evaluationProfileAdapter.submitList(evaluations)
            rvAllEvaluations.adapter = evaluationProfileAdapter
            tvNoEvaluationResult.visibility = View.GONE
        }
        tvAllEvaluationsTitle.text = getString(R.string.all_evaluations, evaluations.size)

    }

    private fun switchToEvaluationCreating() {
        val intent = Intent(this, EvaluationCreatingActivity::class.java)
        intent.putExtra("mentorId", myId)
        intent.putExtra("menteeId", userId)
        startActivity(intent)
    }
}