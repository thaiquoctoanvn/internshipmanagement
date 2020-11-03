package com.example.internshipmanagement.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.UserProfile
import kotlinx.android.synthetic.main.fragment_personal.*
import kotlinx.android.synthetic.main.fragment_profile_editing.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonalFragment : BaseFragment() {

    private val userViewModel by sharedViewModel<UserViewModel>()

    override fun getRootLayoutId(): Int {
        return R.layout.fragment_personal
    }

    override fun setViewOnEventListener() {
        tvLogOut.setOnClickListener { logOut() }
        tvEditProfile.setOnClickListener { switchToEditProfileFragment() }
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
            .centerCrop()
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

    private fun switchToEditProfileFragment() {
        activity?.let {
            it.supportFragmentManager.beginTransaction()
                .add(R.id.frameLayout, ProfileEditingFragment(), "profileEditingFragment")
                .addToBackStack("profileEditingFragment").commit()
        }
    }
}