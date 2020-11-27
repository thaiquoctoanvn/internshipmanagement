package com.example.internshipmanagement.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.Notification
import com.example.internshipmanagement.util.FunctionHelper
import com.example.internshipmanagement.util.SERVER_URL
import kotlinx.android.synthetic.main.item_notification.view.*

class NotificationAdapter :
    ListAdapter<Notification, NotificationAdapter.NotificationViewHolder>(NotificationDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NotificationViewHolder(inflater.inflate(R.layout.item_notification, parent, false))
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(notification: Notification) {
            val time = FunctionHelper.getDateFromTimeMilliSecond(notification.createdAt)
            itemView.apply {
                Glide.with(this)
                    .load("$SERVER_URL${notification.imageUrl}")
                    .placeholder(R.drawable.default_avatar)
                    .circleCrop()
                    .into(ivItemNotification)

                tvBodyItemNotification.text = notification.body
                tvTimeItemNotification.text = time
            }
        }
    }
}

class NotificationDiffUtil : DiffUtil.ItemCallback<Notification>() {
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.body == newItem.body
    }
}