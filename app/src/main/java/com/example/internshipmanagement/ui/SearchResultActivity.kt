package com.example.internshipmanagement.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.UserProfile
import com.example.internshipmanagement.ui.adapter.UserSearchResultAdapter
import com.example.internshipmanagement.ui.base.BaseActivity
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
            } else {
                ibClearAllSearch.visibility = View.VISIBLE
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
        userViewModel.getSearchResultValue().observe(this, Observer {
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
        if(results.isNotEmpty()) {
            tvNoSearchResult.visibility = View.GONE
            userSearchResultAdapter.submitList(results)
            rvUserSearchResult.adapter = userSearchResultAdapter
        } else {
            tvNoSearchResult.visibility = View.VISIBLE
        }

    }

    private val onItemClick: (id: String) -> Unit = {
        val intent = Intent(this, UserProfileActivity::class.java)
        intent.putExtra("userId", it)
        startActivity(intent)
    }
}