package com.example.internshipmanagement.ui.people.mentor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.MyMentee
import com.example.internshipmanagement.ui.MentorViewModel
import com.example.internshipmanagement.ui.userprofile.other.UserProfileActivity
import com.example.internshipmanagement.ui.base.BaseFragment
import com.example.internshipmanagement.ui.people.PeopleViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_all_mentee.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllMenteeFragment : BaseFragment() {

//    private val mentorViewModel by viewModel<MentorViewModel>()
    private val peopleViewModel by sharedViewModel<PeopleViewModel>()

    private lateinit var menteesAdapter: MenteesAdapter

    override fun getRootLayoutId(): Int {
        return R.layout.fragment_all_mentee
    }

    override fun setViewOnEventListener() {
        slAllMentee.setOnRefreshListener { peopleViewModel.getAllMentees() }
    }

    override fun setObserverFragment() {
        peopleViewModel.allMentees.observe(viewLifecycleOwner, Observer {
            updateAllMenteesUI(it)
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        peopleViewModel.getAllMentees()
    }

    private fun updateAllMenteesUI(allMentees: MutableList<MyMentee>) {
        slAllMentee.isRefreshing = false
        if(!this::menteesAdapter.isInitialized) {
            menteesAdapter = MenteesAdapter(onItemClick, onItemOptionIconClick)
        }
        menteesAdapter.submitList(allMentees)
        rvAllMentees.adapter = menteesAdapter
    }

    private val onItemClick: (id: String) -> Unit = {
        val intent = Intent(activity, UserProfileActivity::class.java)
        intent.putExtra("userId", it)
        startActivity(intent)
    }

    private val onItemOptionIconClick: (id: String) -> Unit = { id ->
        val view = LayoutInflater.from(requireActivity()).inflate(R.layout.item_mentee_options, null)
        BottomSheetDialog(requireActivity()).apply {
            setContentView(view)
            view.findViewById<TextView>(R.id.tvAddMenteeDialogTitle).setOnClickListener {
                peopleViewModel.addToMyMentee(id)
                menteesAdapter.notifyItemChanged(peopleViewModel.setIsMyMenteeState(id))
                this.dismiss()
            }
            view.findViewById<TextView>(R.id.tvCancelDialog).setOnClickListener { this.dismiss() }
            show()
        }
    }

}