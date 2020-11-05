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
import kotlinx.android.synthetic.main.item_your_mentee.view.*

class TaskReferencesAdapter(private val onItemClick: (position: Int) -> Unit) : ListAdapter<MyMentee, TaskReferencesAdapter.TaskReferencesViewHolder>(
    TaskReferencesDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskReferencesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return TaskReferencesViewHolder(inflater.inflate(R.layout.item_your_mentee, parent, false))
    }

    override fun onBindViewHolder(holder: TaskReferencesViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    inner class TaskReferencesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(myMentee: MyMentee) {
            Glide.with(itemView)
                .load(myMentee.avatarUrl)
                .circleCrop()
                .placeholder(R.drawable.default_avatar)
                .into(itemView.ivItemYourMentee)
            val name = "${myMentee.menteeName} (${myMentee.menteeNickName})"
            itemView.tvItemYourMenteeName.text = name
            if("true" == myMentee.isReferred) {
                itemView.ivItemYourMenteeCheck.visibility = View.VISIBLE
            } else {
                itemView.ivItemYourMenteeCheck.visibility = View.GONE
            }
            itemView.setOnClickListener { onItemClick(adapterPosition) }
        }
    }
}

class TaskReferencesDiffUtil : DiffUtil.ItemCallback<MyMentee>() {
    override fun areItemsTheSame(oldItem: MyMentee, newItem: MyMentee): Boolean {
        return oldItem.menteeId == newItem.menteeId
    }

    override fun areContentsTheSame(oldItem: MyMentee, newItem: MyMentee): Boolean {
        return oldItem.menteeNickName == newItem.menteeNickName
    }

}