package com.example.internshipmanagement.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.MyMentor
import com.example.internshipmanagement.util.SERVER_URL
import kotlinx.android.synthetic.main.item_user_search_result.view.*

class MyMentorAdapter(
    private val onItemClick: (id: String) -> Unit
) : ListAdapter<MyMentor, MyMentorAdapter.MyMentorViewHolder>(MyMentorDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyMentorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyMentorViewHolder(inflater.inflate(R.layout.item_user_search_result, parent, false))
    }

    override fun onBindViewHolder(holder: MyMentorViewHolder, position: Int) {
        holder.binData(getItem(position))
    }

    inner class MyMentorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun binData(myMentor: MyMentor) {
            val name = "${myMentor.mentorName} (${myMentor.mentorNickName})"
            itemView.apply {
                tvItemUserSearchResultName.text = name
                tvItemUserSearchResultType.text = "Mentor"
                tvItemUserSearchResultPosition.text = myMentor.position

                Glide.with(this)
                    .load("$SERVER_URL${myMentor.avatarUrl}")
                    .placeholder(R.drawable.default_avatar)
                    .circleCrop()
                    .into(ivItemUserSearchResult)
                setOnClickListener { onItemClick(myMentor.mentorId) }
            }
        }
    }
}

class MyMentorDiffUtil : DiffUtil.ItemCallback<MyMentor>() {
    override fun areItemsTheSame(oldItem: MyMentor, newItem: MyMentor): Boolean {
        return oldItem.mentorId == newItem.mentorId
    }

    override fun areContentsTheSame(oldItem: MyMentor, newItem: MyMentor): Boolean {
        return oldItem.mentorName == newItem.mentorName
    }
}