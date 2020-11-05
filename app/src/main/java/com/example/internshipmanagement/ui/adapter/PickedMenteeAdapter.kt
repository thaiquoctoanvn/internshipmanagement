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
import kotlinx.android.synthetic.main.item_picked_mentee.view.*

class PickedMenteeAdapter(
    private val onItemRemoveClick: (position: Int, id: String) -> Unit
) : ListAdapter<MyMentee, PickedMenteeAdapter.PickedMenteeViewHoler>(PickedMenteeDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickedMenteeViewHoler {
        val inflater = LayoutInflater.from(parent.context)
        return PickedMenteeViewHoler(inflater.inflate(R.layout.item_picked_mentee, parent, false))
    }

    override fun onBindViewHolder(holder: PickedMenteeViewHoler, position: Int) {
        holder.bindData(getItem(position))
    }

    inner class PickedMenteeViewHoler(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(myMentee: MyMentee) {
            val name = "${myMentee.menteeName} (${myMentee.menteeNickName})"
            itemView.tvNameItemPickedMentee.text = name
            itemView.ibRemoveItemPickedMentee.setOnClickListener {
                onItemRemoveClick(adapterPosition, myMentee.menteeId)
            }
            Glide.with(itemView)
                .load(myMentee.avatarUrl)
                .placeholder(R.drawable.default_avatar)
                .circleCrop()
                .into(itemView.ivPickedMentee)
        }
    }
}

class PickedMenteeDiffUtil : DiffUtil.ItemCallback<MyMentee>() {
    override fun areItemsTheSame(oldItem: MyMentee, newItem: MyMentee): Boolean {
        return oldItem.menteeId == newItem.menteeId
    }

    override fun areContentsTheSame(oldItem: MyMentee, newItem: MyMentee): Boolean {
        return oldItem.isReferred == newItem.isReferred
    }
}