package com.example.internshipmanagement.ui.taskdetail.mentor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.TaskReference
import com.example.internshipmanagement.util.SERVER_URL
import kotlinx.android.synthetic.main.item_mentor_task_detail_refer.view.*

class MentorTaskDetailAdapter(
    private val onItemClick: (referenceId: String) -> Unit
) : ListAdapter<TaskReference, MentorTaskDetailAdapter.MentorTaskDetailViewHolder>(
    MentorTaskDetailDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorTaskDetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MentorTaskDetailViewHolder(inflater.inflate(R.layout.item_mentor_task_detail_refer, parent, false))
    }

    override fun onBindViewHolder(holder: MentorTaskDetailViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    inner class MentorTaskDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(taskReference: TaskReference) {
            itemView.apply {
                val name = "${taskReference.name} (${taskReference.nickName})"
                tvItemMenteeName.text = name
                Glide.with(this)
                    .load("$SERVER_URL${taskReference.avatarUrl}")
                    .circleCrop()
                    .placeholder(R.drawable.default_avatar)
                    .into(ivItemMentee)
                if(taskReference.isSubmitted == "1") {
                    tvStateReferTaskDetail.text = context.getString(R.string.submitted_state)
                } else {
                    tvStateReferTaskDetail.text = context.getString(R.string.not_submitted_state)
                }
                if(taskReference.isReviewed == "1") {
                    tvReviewedState.visibility = View.VISIBLE
                } else {
                    tvReviewedState.visibility = View.GONE
                }

                setOnClickListener { onItemClick(taskReference.id) }
            }
        }
    }
}

class MentorTaskDetailDiffUtil : DiffUtil.ItemCallback<TaskReference>() {
    override fun areItemsTheSame(oldItem: TaskReference, newItem: TaskReference): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TaskReference, newItem: TaskReference): Boolean {
        return oldItem.isReviewed == newItem.isReviewed
    }
}