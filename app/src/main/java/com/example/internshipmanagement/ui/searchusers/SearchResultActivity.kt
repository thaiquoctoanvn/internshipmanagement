package com.example.internshipmanagement.ui.searchusers

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.UserProfile
import com.example.internshipmanagement.ui.UserViewModel
import com.example.internshipmanagement.ui.base.BaseActivity
import com.example.internshipmanagement.ui.userprofile.other.UserProfileActivity
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchResultActivity : BaseActivity() {

    private val userViewModel by viewModel<UserViewModel>()

    private lateinit var userSearchResultAdapter: UserSearchResultAdapter

    override fun getActivityRootLayout(): Int {
        return R.layout.activity_search_result
    }

    override fun setViewOnEventListener() {
        etSearchUser.doAfterTextChanged {
            if(TextUtils.isEmpty(it)) {
                ibClearAllSearch.visibility = View.GONE
                pbSearchLoading.visibility = View.GONE
            } else {
                ibClearAllSearch.visibility = View.VISIBLE
                pbSearchLoading.visibility = View.VISIBLE
                CoroutineScope(Dispatchers.IO).launch {
                    delay(300)
                    userViewModel.searchAllUsers(it.toString())
                }
            }
        }
        ibClearAllSearch.setOnClickListener { etSearchUser.setText("") }
        tvSearchUserCancel.setOnClickListener { this.finish() }
    }

    override fun setObserver() {
        userViewModel.searchResult.observe(this, Observer {
            updateSearchResultUI(it)
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setBaseObserver(userViewModel)
    }

    private fun updateSearchResultUI(results: MutableList<UserProfile>) {
        if(!this::userSearchResultAdapter.isInitialized) {
            userSearchResultAdapter = UserSearchResultAdapter(onItemClick)
        }
        pbSearchLoading.visibility = View.GONE
        if(results.isNotEmpty()) {
            tvNoSearchResult.visibility = View.GONE
            rvUserSearchResult.visibility = View.VISIBLE
            userSearchResultAdapter.submitList(results)
            rvUserSearchResult.adapter = userSearchResultAdapter
        } else {
            tvNoSearchResult.visibility = View.VISIBLE
            rvUserSearchResult.visibility = View.GONE
        }

    }

    private val onItemClick: (id: String) -> Unit = {
        val intent = Intent(this, UserProfileActivity::class.java)
        intent.putExtra("userId", it)
        startActivity(intent)
    }
}