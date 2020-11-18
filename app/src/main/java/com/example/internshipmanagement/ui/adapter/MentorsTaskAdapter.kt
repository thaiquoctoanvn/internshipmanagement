package com.example.internshipmanagement.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.MentorsTask
import com.example.internshipmanagement.util.FunctionHelper
import kotlinx.android.synthetic.main.item_mentors_task.view.*

class MentorsTaskAdapter(private val onItemClick: (task: MentorsTask) -> Unit) : ListAdapter<MentorsTask, MentorsTaskAdapter.MentorsTaskViewHolder>(MentorsTaskDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorsTaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MentorsTaskViewHolder(inflater.inflate(R.layout.item_mentors_task, parent, false))
    }

    override fun onBindViewHolder(holder: MentorsTaskViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    inner class MentorsTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(mentorsTask: MentorsTask) {
            itemView.tvItemMentorTaskName.text = mentorsTask.content
            itemView.tvItemMentorTaskDeadline.text = FunctionHelper.getDateFromTimeMilliSecond(mentorsTask.deadline)
            val state = "${mentorsTask.numberSubmitted} / ${mentorsTask.referenceNumber} submitted"
            itemView.tvItemMentorTaskStatus.text = state

            itemView.setOnClickListener { onItemClick(mentorsTask) }
        }
    }
}

class MentorsTaskDiffUtil : DiffUtil.ItemCallback<MentorsTask>() {
    override fun areItemsTheSame(oldItem: MentorsTask, newItem: MentorsTask): Boolean {
        return oldItem.taskId == newItem.taskId
    }

    override fun areContentsTheSame(oldItem: MentorsTask, newItem: MentorsTask): Boolean {
        return oldItem.isClosed == newItem.isClosed
    }

}