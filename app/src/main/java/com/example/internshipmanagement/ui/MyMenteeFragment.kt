package com.example.internshipmanagement.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.MyMentee
import com.example.internshipmanagement.ui.adapter.MenteesAdapter
import com.example.internshipmanagement.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_my_mentee.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyMenteeFragment : BaseFragment() {

    private val mentorViewModel by viewModel<MentorViewModel>()

    private lateinit var menteesAdapter: MenteesAdapter

    override fun getRootLayoutId(): Int {
        return R.layout.fragment_my_mentee
    }

    override fun setViewOnEventListener() {

    }

    override fun setObserverFragment() {
        mentorViewModel.getMyMenteesValue().observe(viewLifecycleOwner, Observer {
            updateMyMenteesUI(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mentorViewModel.getMyMentees()
    }

    private fun updateMyMenteesUI(myMentees: MutableList<MyMentee>) {
        if(!this::menteesAdapter.isInitialized) {
            menteesAdapter = MenteesAdapter(onItemClick, onItemOptionIconClick)
        }
        menteesAdapter.submitList(myMentees)
        rvMyMentees.adapter = menteesAdapter
    }

    private val onItemClick: (id: String) -> Unit = {
        val intent = Intent(activity, UserProfileActivity::class.java)
        intent.putExtra("userId", it)
        startActivity(intent)
    }

    private val onItemOptionIconClick: (id: String) -> Unit = {

    }

}