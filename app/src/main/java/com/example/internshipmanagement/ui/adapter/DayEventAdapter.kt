package com.example.internshipmanagement.ui.adapter

import android.content.res.ColorStateList
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.DayEvent
import com.example.internshipmanagement.util.FunctionHelper
import kotlinx.android.synthetic.main.item_day_event.view.*
import kotlinx.android.synthetic.main.item_mentees_task.view.*

class DayEventAdapter : ListAdapter<DayEvent, DayEventAdapter.DayEventViewHolder>(DayEventDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayEventViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DayEventViewHolder(inflater.inflate(R.layout.item_day_event, parent, false))
    }

    override fun onBindViewHolder(holder: DayEventViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    inner class DayEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(event: DayEvent) {
            val deadline = "Deadline: ${FunctionHelper.getDateFromTimeMilliSecond(event.deadline)}"
            val owner = "Created by Mentor ${event.taskOwner}"
            if(event.deadline.toLong() < System.currentTimeMillis()) {
                itemView.tvItemEventIndex.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.mentor_strong_color))
            } else {
                itemView.tvItemEventIndex.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.mentee_strong_color))
            }

            if(TextUtils.isEmpty(event.taskOwner)) {
                itemView.apply {
                    tvItemEventOwner.visibility = View.GONE
                    ivItemEventOwner.visibility = View.GONE
                }
            } else {
                itemView.apply {
                    tvItemEventOwner.visibility = View.VISIBLE
                    ivItemEventOwner.visibility = View.VISIBLE
                }
            }
            itemView.apply {
                tvItemEventName.text = event.taskContent
                tvItemEventDeadline.text = deadline
                tvItemEventOwner.text = owner
                Glide.with(this)
                    .load(event.ownerAvatar)
                    .placeholder(R.drawable.default_avatar)
                    .circleCrop()
                    .into(ivItemEventOwner)
            }
        }
    }
}

class DayEventDiffUtil : DiffUtil.ItemCallback<DayEvent>() {
    override fun areItemsTheSame(oldItem: DayEvent, newItem: DayEvent): Boolean {
        return oldItem.taskId == newItem.taskId
    }

    override fun areContentsTheSame(oldItem: DayEvent, newItem: DayEvent): Boolean {
        return oldItem.taskContent == newItem.taskContent
    }
}