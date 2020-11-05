package com.example.internshipmanagement.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.UserProfile
import com.example.internshipmanagement.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_personal.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PersonalFragment : BaseFragment() {

    private val userViewModel by sharedViewModel<UserViewModel>()

    override fun getRootLayoutId(): Int {
        return R.layout.fragment_personal
    }

    override fun setViewOnEventListener() {
        tvLogOut.setOnClickListener { logOut() }
        tvEditProfile.setOnClickListener { switchToEditProfileActivity() }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        retrievePersonalInfo()
        super.setBaseObserverFragment(userViewModel)
        if(super.isMentorAccount(userViewModel.getSharedPref().getString("type", "")!!)) {
            ivIconEvaluation.visibility = View.GONE
            tvProfileEvaluation.visibility = View.GONE
        }
    }

    private fun retrievePersonalInfo() {
        val id = userViewModel.getSharedPref().getString("userId", "")
        Log.d("###", "userId: $id")
        if (id != null) {
            Log.d("###", "retrieving data")
            userViewModel.getUserProfile(id)
        }
    }

    private fun updateUI(userProfile: UserProfile) {
        tvProfileName.text = userProfile.name
        tvNickName.text = "@" + userProfile.nickName
        tvProfilePosition.text = userProfile.role
        tvProfileEmail.text = userProfile.email
        Glide.with(this)
            .load(userProfile.avatarUrl)
            .circleCrop()
            .placeholder(R.drawable.default_avatar)
            .into(ivAvatarProfile)
    }

    private fun setObserver() {
        userViewModel.getUserProfileValue().observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })
    }

    private fun logOut() {
        userViewModel.getSharedPref().edit().apply {
            remove("userId")
            remove("token")
            remove("type")
            remove("avatarUrl")
        }.apply()
        startActivity(Intent(activity, LogInActivity::class.java))
        activity?.finish()
    }

    private fun switchToEditProfileActivity() {
//        activity?.let {
//            it.supportFragmentManager.beginTransaction()
//                .add(R.id.frameLayout, ProfileEditingFragment(), "profileEditingFragment")
//                .addToBackStack("profileEditingFragment").commit()
//            super.hideBottomNavigationView(it)
//        }
        val intent = Intent(activity, ProfileEditingActivity::class.java)
        intent.putExtra("userId", userViewModel.getSharedPref().getString("userId", ""))
        startActivity(intent)
    }
}