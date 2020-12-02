package com.example.internshipmanagement.ui.people.mentor

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.MyMentee
import com.example.internshipmanagement.ui.MentorViewModel
import com.example.internshipmanagement.ui.userprofile.other.UserProfileActivity
import com.example.internshipmanagement.ui.base.BaseFragment
import com.example.internshipmanagement.ui.people.PeopleViewModel
import kotlinx.android.synthetic.main.fragment_my_mentee.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyMenteeFragment : BaseFragment() {

//    private val mentorViewModel by viewModel<MentorViewModel>()
    private val peopleViewModel by sharedViewModel<PeopleViewModel>()

    private lateinit var menteesAdapter: MenteesAdapter

    override fun getRootLayoutId(): Int {
        return R.layout.fragment_my_mentee
    }

    override fun setViewOnEventListener() {
        slMyMentee.setOnRefreshListener { peopleViewModel.getMyMentees() }
    }

    override fun setObserverFragment() {
        peopleViewModel.myMentees.observe(viewLifecycleOwner, Observer {
            updateMyMenteesUI(it)
        })
        peopleViewModel.myNewMenteePosition.observe(viewLifecycleOwner, Observer {
            menteesAdapter.apply {
                notifyItemInserted(it)
                notifyItemChanged(it)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        peopleViewModel.getMyMentees()
    }

    private fun updateMyMenteesUI(myMentees: MutableList<MyMentee>) {
        slMyMentee.isRefreshing = false
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