package com.example.internshipmanagement.ui

import android.app.DatePickerDialog
import android.location.Criteria
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.Criterion
import com.example.internshipmanagement.data.entity.UserProfile
import com.example.internshipmanagement.ui.adapter.CriterionAdapter
import com.example.internshipmanagement.ui.base.BaseActivity
import com.example.internshipmanagement.util.DataHelper
import com.example.internshipmanagement.util.FunctionHelper
import kotlinx.android.synthetic.main.activity_evaluation_creating.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class EvaluationCreatingActivity : BaseActivity() {

    private lateinit var criterionAdapter: CriterionAdapter

    private val mentorViewModel by viewModel<MentorViewModel>()
    private val userViewModel by viewModel<UserViewModel>()

    private var mentorId = ""
    private var menteeId = ""

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_evaluation_creating
    }

    override fun setViewOnEventListener() {
        ibPickedFromDate.setOnClickListener { openDatePickerDialog(true) }
        ibPickedToDate.setOnClickListener { openDatePickerDialog(false) }
        ibEvaluationCreatingCancel.setOnClickListener { this.finish() }
        tvEvaluationCreatingSave.setOnClickListener { addEvaluation() }
    }

    override fun setObserver() {
        mentorViewModel.getCriteriaValue().observe(this, androidx.lifecycle.Observer {
            loadCriteria(it)
        })
        mentorViewModel.getIsSucceedValue().observe(this, androidx.lifecycle.Observer {
            completeEvaluationAdding(it)
        })
        userViewModel.getUserProfileValue().observe(this, androidx.lifecycle.Observer {
            updateMentorInfoUI(it)
            mentorViewModel.generateCriteria()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setBaseObserver(mentorViewModel)
        loadMentorInfo()
    }

    private fun updateMentorInfoUI(userProfile: UserProfile) {
        val name = "Mentor ${userProfile.name} (${userProfile.nickName})"
        tvMentorName.text = name
    }

    private fun loadCriteria(criteria: MutableList<Criterion>) {
        if(!this::criterionAdapter.isInitialized) {
            criterionAdapter = CriterionAdapter(onItemCriterionMarkChange)
        }
        criterionAdapter.submitList(criteria)
        rvCriteria.adapter = criterionAdapter
    }

    private fun loadMentorInfo() {
        val intentData = intent
        if(intentData != null) {
            mentorId = intentData.getStringExtra("mentorId").toString()
            menteeId = intentData.getStringExtra("menteeId").toString()
            if(mentorId.isNotEmpty() && menteeId.isNotEmpty()) {
                userViewModel.getUserProfile(mentorId)
            }
        }
    }

    private fun openDatePickerDialog(isFromDate: Boolean) {
        val currentDate = Calendar.getInstance()
        val currentYear = currentDate.get(Calendar.YEAR)
        val currentMonth = currentDate.get(Calendar.MONTH)
        val currentDay = currentDate.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            R.style.MySpinnerDatePickerStyle,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val resultDate = "$dayOfMonth/${monthOfYear + 1}/$year"
                if(isFromDate) {
                    tvPickedFromDate.text = resultDate
                } else {
                    tvPickedToDate.text = resultDate
                }
            },
            currentYear,
            currentMonth,
            currentDay
        ).show()
    }

    private fun addEvaluation() {
        val currentTime = System.currentTimeMillis()
        val fromDate = FunctionHelper.getMilliSecondFromDate(tvPickedFromDate.text.toString().trim())
        val toDate = FunctionHelper.getMilliSecondFromDate(tvPickedToDate.text.toString().trim())
        if(fromDate != null && toDate != null) {
            val criteriaMark = mentorViewModel.calculateCriteriaMark()
            if(fromDate >= currentTime) {
                super.showSnackBar("From date must be lower now")
                return
            }
            if(fromDate >= toDate - 90000000 * 20) {
                super.showSnackBar("From date and To date must be 20 days apart")
                return
            }
            if(criteriaMark == null) {
                super.showSnackBar("Invalid mark for a criterion")
                return
            }
            if(mentorId.isEmpty() || menteeId.isEmpty()) {
                super.showSnackBar("Cannot create evaluation")
                return
            }
            mentorViewModel.addEvaluation(
                mentorId,
                menteeId,
                fromDate.toString(),
                toDate.toString(),
                etComment.text.toString().trim(),
                criteriaMark.toString()
            )
        } else {
            super.showSnackBar("Date must be not empty")
        }
    }

    private fun completeEvaluationAdding(isSucceed: Boolean) {
        if(isSucceed) {
            this.finish()
        } else {
            Log.d("###", "Create evaluation failed")
        }
    }

    private val onItemCriterionMarkChange: (mark: String) -> Unit = {mark: String -> }
}