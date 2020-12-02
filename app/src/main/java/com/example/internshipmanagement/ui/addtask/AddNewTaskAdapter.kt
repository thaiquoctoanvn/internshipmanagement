package com.example.internshipmanagement.ui.addtask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.MyMentee
import com.example.internshipmanagement.util.SERVER_URL
import kotlinx.android.synthetic.main.item_your_mentee.view.*

class AddNewTaskAdapter : ListAdapter<MyMentee, AddNewTaskAdapter.ReferencesViewHolder>(
    AddNewTaskAdapterDiffUtil()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReferencesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ReferencesViewHolder(inflater.inflate(R.layout.item_your_mentee, parent, false))
    }

    override fun onBindViewHolder(holder: ReferencesViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    inner class ReferencesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(myMentee: MyMentee) {
            Glide.with(itemView)
                .load("$SERVER_URL${myMentee.avatarUrl}")
                .circleCrop()
                .placeholder(R.drawable.default_avatar)
                .into(itemView.ivItemYourMentee)
            val name = "${myMentee.menteeName} (${myMentee.menteeNickName})"
            itemView.tvItemYourMenteeName.text = name
            itemView.ivItemYourMenteeCheck.visibility = View.GONE
        }
    }
}

class AddNewTaskAdapterDiffUtil : DiffUtil.ItemCallback<MyMentee>() {
    override fun areItemsTheSame(oldItem: MyMentee, newItem: MyMentee): Boolean {
        return oldItem.menteeId == newItem.menteeId
    }

    override fun areContentsTheSame(oldItem: MyMentee, newItem: MyMentee): Boolean {
        return oldItem.menteeNickName == newItem.menteeNickName && oldItem.menteeName == newItem.menteeName
    }

}