package com.example.internshipmanagement.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeTransition
import com.example.internshipmanagement.R
import com.example.internshipmanagement.util.SERVER_URL
import kotlinx.android.synthetic.main.item_full_image.view.*

class FullImageAdapter : ListAdapter<String, FullImageAdapter.FullImageViewHolder>(FullImageDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FullImageViewHolder(inflater.inflate(R.layout.item_full_image, parent, false))
    }

    override fun onBindViewHolder(holder: FullImageViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    inner class FullImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(url: String) {
            val requestOption = RequestOptions().apply {
                placeholder(R.drawable.app_logo)
                error(R.drawable.ic_close)
                fitCenter()
            }
            Glide.with(itemView)
                .load("$SERVER_URL$url")
                .apply(requestOption)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.ivFullImage)
        }
    }
}

class FullImageDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}