package com.example.internshipmanagement.ui.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.UserProfile
import kotlinx.android.synthetic.main.item_user_search_result.view.*

class UserSearchResultAdapter(
    private val onItemClick: (id: String) -> Unit
) : ListAdapter<UserProfile, UserSearchResultAdapter.UserSearchResultViewHolder>(UserSearchResultDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserSearchResultViewHolder(inflater.inflate(R.layout.item_user_search_result, parent, false))
    }

    override fun onBindViewHolder(holder: UserSearchResultViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    inner class UserSearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(userProfile: UserProfile) {
            val name = "${userProfile.name} (${userProfile.nickName})"
            val type = if(userProfile.type == "1") "Mentor" else "Mentee"
            if(TextUtils.isEmpty(userProfile.role)) {
                itemView.tvItemUserSearchResultPosition.visibility = View.GONE
            } else {
                itemView.tvItemUserSearchResultPosition.apply {
                    visibility = View.VISIBLE
                    text = userProfile.role
                }
            }
            itemView.tvItemUserSearchResultName.text = name
            itemView.tvItemUserSearchResultType.text = type

            itemView.setOnClickListener { onItemClick(userProfile.userId) }
        }
    }
}

class UserSearchResultDiffUtil : DiffUtil.ItemCallback<UserProfile>() {
    override fun areItemsTheSame(oldItem: UserProfile, newItem: UserProfile): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: UserProfile, newItem: UserProfile): Boolean {
        return oldItem.name == newItem.name
    }
}