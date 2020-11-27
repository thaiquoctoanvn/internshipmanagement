package com.example.internshipmanagement.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.MyMentor
import com.example.internshipmanagement.ui.adapter.MyMentorAdapter
import com.example.internshipmanagement.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_mentee_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MenteeSearchFragment : BaseFragment() {

    private val menteeViewModel by viewModel<MenteeViewModel>()

    override fun getRootLayoutId(): Int {
        return R.layout.fragment_mentee_search
    }

    override fun setViewOnEventListener() {
        ibSearchUser.setOnClickListener { startActivity(Intent(activity, SearchResultActivity::class.java)) }
    }

    override fun setObserverFragment() {
        menteeViewModel.myMentor.observe(requireActivity(), Observer {
            updateMyMentorContainer(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menteeViewModel.getMyMentor()
    }

    private fun updateMyMentorContainer(myMentors: MutableList<MyMentor>) {
        pbSearchLoading.visibility = View.GONE
        if(myMentors.size == 0) {
            rvMyMentor.visibility = View.GONE
            tvMenteeSearchNoData.visibility = View.VISIBLE
        } else {
            rvMyMentor.visibility = View.VISIBLE
            tvMenteeSearchNoData.visibility = View.GONE

            val myMentorAdapter = MyMentorAdapter(onItemClick)
            myMentorAdapter.submitList(myMentors)
            rvMyMentor.adapter = myMentorAdapter
        }
    }

    private val onItemClick: (id: String) -> Unit = {
        val intent = Intent(activity, UserProfileActivity::class.java)
        intent.putExtra("userId", it)
        startActivity(intent)
    }

}