package com.example.internshipmanagement.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.MyMentee
import com.example.internshipmanagement.ui.MenteeViewModel
import com.example.internshipmanagement.util.SERVER_URL
import kotlinx.android.synthetic.main.item_your_mentee.view.*

class MenteesAdapter(
    private val onItemClick: (id: String) -> Unit,
    private val onItemOptionIconClick: (id: String) -> Unit
) : ListAdapter<MyMentee, MenteesAdapter.MenteesViewHolder>(MenteesDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenteesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MenteesViewHolder(inflater.inflate(R.layout.item_your_mentee, parent, false))
    }

    override fun onBindViewHolder(holder: MenteesViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    inner class MenteesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(myMentee: MyMentee) {
            val name = "${myMentee.menteeName} (${myMentee.menteeNickName})"
            itemView.ivItemYourMenteeCheck.visibility = View.GONE
            itemView.ivItemYourMenteeOption.visibility = View.VISIBLE
            itemView.tvItemYourMenteeName.text = name
            Glide.with(itemView)
                .load("$SERVER_URL${myMentee.avatarUrl}")
                .placeholder(R.drawable.default_avatar)
                .circleCrop()
                .into(itemView.ivItemYourMentee)
            itemView.setOnClickListener { onItemClick(myMentee.menteeId) }
            itemView.ivItemYourMenteeOption.setOnClickListener { onItemOptionIconClick(myMentee.menteeId) }
        }
    }
}

class MenteesDiffUtil : DiffUtil.ItemCallback<MyMentee>() {
    override fun areItemsTheSame(oldItem: MyMentee, newItem: MyMentee): Boolean {
        return oldItem.menteeId == newItem.menteeId
    }

    override fun areContentsTheSame(oldItem: MyMentee, newItem: MyMentee): Boolean {
        return oldItem.menteeName == newItem.menteeName
    }
}