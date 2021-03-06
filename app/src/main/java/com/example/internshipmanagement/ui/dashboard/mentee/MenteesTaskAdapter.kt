package com.example.internshipmanagement.ui.dashboard.mentee

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.MenteesTask
import com.example.internshipmanagement.util.FunctionHelper
import kotlinx.android.synthetic.main.item_mentees_task.view.*

class MenteesTaskAdapter(
    private val onItemClick: (taskId: String) -> Unit
) : ListAdapter<MenteesTask, MenteesTaskAdapter.MenteesTaskViewHolder>(MenteesTaskDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenteesTaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MenteesTaskViewHolder((inflater.inflate(R.layout.item_mentees_task, parent, false)))
    }

    override fun onBindViewHolder(holder: MenteesTaskViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    inner class MenteesTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(menteesTask: MenteesTask) {
            val deadline = "Deadline: ${FunctionHelper.getDateFromTimeMilliSecond(menteesTask.deadline)}"
            itemView.tvItemMenteeTaskName.text = menteesTask.content
            itemView.tvItemMenteesTaskDeadline.text = deadline

            if(menteesTask.isReviewed == "0") {
                itemView.tvItemMenteesTaskReviewedState.visibility = View.GONE
            } else {
                itemView.tvItemMenteesTaskReviewedState.visibility = View.VISIBLE
                itemView.tvIconIndex.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.mentor_strong_color))
            }

            if(menteesTask.isSubmitted == "0") {
                itemView.tvMenteesTaskSubmittedState.visibility = View.GONE
            } else {
                itemView.tvMenteesTaskSubmittedState.visibility = View.VISIBLE
            }
            itemView.setOnClickListener { onItemClick(menteesTask.taskId) }
        }
    }
}

class MenteesTaskDiffUtil : DiffUtil.ItemCallback<MenteesTask>() {
    override fun areItemsTheSame(oldItem: MenteesTask, newItem: MenteesTask): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MenteesTask, newItem: MenteesTask): Boolean {
        return oldItem.isSubmitted == newItem.isSubmitted
    }
}