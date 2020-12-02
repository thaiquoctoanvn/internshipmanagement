package com.example.internshipmanagement.ui.userprofile.personal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.UserProfile
import com.example.internshipmanagement.ui.evaluationprofile.EvaluationProfileActivity
import com.example.internshipmanagement.ui.login.LogInActivity
import com.example.internshipmanagement.ui.profileediting.ProfileEditingActivity
import com.example.internshipmanagement.ui.UserViewModel
import com.example.internshipmanagement.ui.base.BaseFragment
import com.example.internshipmanagement.ui.userprofile.TabStatisticAdapter
import com.example.internshipmanagement.util.INFO_UPDATED
import com.example.internshipmanagement.util.SERVER_URL
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_personal.*
import kotlinx.android.synthetic.main.fragment_personal.ivAvatarProfile
import kotlinx.android.synthetic.main.fragment_personal.tabLayoutPieChart
import kotlinx.android.synthetic.main.fragment_personal.tvNickName
import kotlinx.android.synthetic.main.fragment_personal.tvProfileEmail
import kotlinx.android.synthetic.main.fragment_personal.tvProfileName
import kotlinx.android.synthetic.main.fragment_personal.tvProfilePosition
import kotlinx.android.synthetic.main.fragment_personal.vpPieChart
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonalFragment : BaseFragment() {

    private lateinit var tabStatisticAdapter: TabStatisticAdapter
    private lateinit var infoUpdatePush: BroadcastReceiver
    private lateinit var personalOptionsView: View
    private lateinit var bottomSheetDialog: BottomSheetDialog

    private val userViewModel by viewModel<UserViewModel>()


    override fun getRootLayoutId(): Int {
        return R.layout.fragment_personal
    }

    override fun setViewOnEventListener() {
        ibPersonalOption.setOnClickListener { showPersonalOptions() }
        tvEditProfile.setOnClickListener { switchToEditProfileActivity() }
        tvProfileEvaluation.setOnClickListener { switchToEvaluationProfile() }
        slPersonalFragment.setOnRefreshListener { retrievePersonalInfo() }
        tabLayoutPieChart.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        appBarPersonalFragment.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            // Chỉ enable swipe layout khi trở lên đầu màn hình
            val rect = android.graphics.Rect()
            slPersonalFragment.isEnabled = ivCover.getGlobalVisibleRect(rect) && ivCover.height == rect.height()
        })
    }

    override fun setObserverFragment() {
        userViewModel.userProfile.observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })
        userViewModel.isSuccessful.observe(viewLifecycleOwner, Observer {
            finishSession(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenBroadcast()
        registerBroadcast()
        retrievePersonalInfo()
        super.setBaseObserverFragment(userViewModel)
    }



    private fun retrievePersonalInfo() {
        val id = userViewModel.getMyAccountId()
        if (id != null) {
            userViewModel.getUserProfile(id)
        }
    }

    private fun updateUI(userProfile: UserProfile) {
        slPersonalFragment.isRefreshing = false
        if(super.isMentorAccount(userProfile.type)) {
            ivIconEvaluation.visibility = View.GONE
            tvProfileEvaluation.visibility = View.GONE
            tabLayoutPieChart.visibility = View.GONE
            nsvPersonalFragment.visibility = View.GONE
        } else {
            launchTab()
        }
        val nickName = "@" + userProfile.nickName
        tvProfileName.text = userProfile.name
        tvNickName.text = nickName
        tvProfilePosition.text = userProfile.role
        tvProfileEmail.text = userProfile.email
        Glide.with(this)
            .load("$SERVER_URL${userProfile.avatarUrl}")
            .circleCrop()
            .placeholder(R.drawable.default_avatar)
            .into(ivAvatarProfile)
    }

    private fun finishSession(isLogOut: Boolean) {
        if(isLogOut) {
            startActivity(Intent(activity, LogInActivity::class.java))
            activity?.finish()
        } else {
            super.showSnackBar("Log out failed")
        }
    }

    private fun logOut() {
        userViewModel.logOut()
    }

    private fun switchToEditProfileActivity() {
        val intent = Intent(activity, ProfileEditingActivity::class.java)
        intent.putExtra("userId", userViewModel.getSharedPref().getString("userId", ""))
        startActivity(intent)
    }

    private fun switchToEvaluationProfile() {
        val intent = Intent(requireActivity(), EvaluationProfileActivity::class.java)
        intent.putExtra("userId", userViewModel.getSharedPref().getString("userId", ""))
        intent.putExtra("myId", userViewModel.getSharedPref().getString("userId", ""))
        startActivity(intent)
    }

    private fun listenBroadcast() {
        infoUpdatePush = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = userViewModel.getSharedPref().getString("userId", "")
                if (id != null) {
                    userViewModel.getUserProfile(id)
                }
            }
        }
    }

    private fun showPersonalOptions() {
        if(!this::personalOptionsView.isInitialized) {
            personalOptionsView = LayoutInflater.from(requireActivity()).inflate(R.layout.item_log_out, null)
        }
        if(!this::bottomSheetDialog.isInitialized) {
            bottomSheetDialog = BottomSheetDialog(requireActivity())
        }

        bottomSheetDialog.apply {
            setContentView(personalOptionsView)
            personalOptionsView.findViewById<TextView>(R.id.tvLogOutDialog).setOnClickListener {
                this.dismiss()
                logOut()
            }
            personalOptionsView.findViewById<TextView>(R.id.tvCancelDialog).setOnClickListener { this.dismiss() }
            show()
        }
    }

    private fun registerBroadcast() {
        activity?.registerReceiver(infoUpdatePush, IntentFilter(INFO_UPDATED))
    }

    private fun launchTab() {
        if(!this::tabStatisticAdapter.isInitialized) {
            tabStatisticAdapter = TabStatisticAdapter(childFragmentManager, this.lifecycle)
        }
        vpPieChart.adapter = tabStatisticAdapter
        TabLayoutMediator(tabLayoutPieChart, vpPieChart) { tab: TabLayout.Tab, position: Int ->
            when(position) {
                0 -> tab.text = "Criteria statistic"
                else -> tab.text = "Task statistic"
            }
        }.attach()
    }
}