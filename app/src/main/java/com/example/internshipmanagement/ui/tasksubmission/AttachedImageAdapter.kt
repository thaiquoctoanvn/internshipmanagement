package com.example.internshipmanagement.ui.tasksubmission

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import kotlinx.android.synthetic.main.item_attached_image.view.*
import kotlinx.coroutines.*

class AttachedImageAdapter(
     val onItemRemove: (position: Int) -> Unit
) : ListAdapter<String, AttachedImageAdapter.AttachedImageViewHolder>(AttachedImageDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachedImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AttachedImageViewHolder(
            inflater.inflate(
                R.layout.item_attached_image,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AttachedImageViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    inner class AttachedImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(item: String) {
            CoroutineScope(Dispatchers.IO).launch {
                val resizedBitmap = scaleBitmapFromUri(item)
                withContext(Dispatchers.Main) {
                    Glide.with(itemView)
                        .load(resizedBitmap)
                        .placeholder(R.drawable.app_logo)
                        .error(R.drawable.app_logo)
                        .into(itemView.ivAttachedImage)

                }
            }
            itemView.ibAttachedImageRemove.setOnClickListener { onItemRemove(adapterPosition) }
        }

        private fun scaleBitmapFromUri(uri: String): Bitmap? {
            val requiredSize = 720
            val inputStream = itemView.context.contentResolver.openInputStream(Uri.parse(uri))
            val srcBitmap = BitmapFactory.decodeStream(inputStream)
            var srcWidth = srcBitmap.width
            var srcHeight = srcBitmap.height

            while (srcWidth / 2 >= requiredSize || srcHeight / 2 >= requiredSize) {
                srcWidth /= 2
                srcHeight /= 2

            }
            return Bitmap.createScaledBitmap(srcBitmap, srcWidth, srcHeight, true)
        }

        private fun scalePureBitmap(srcBitmap: Bitmap): Bitmap? {
            val requiredSize = 720
            var srcWidth = srcBitmap.width
            var srcHeight = srcBitmap.height

            while (srcWidth / 2 >= requiredSize || srcHeight / 2 >= requiredSize) {
                srcWidth /= 2
                srcHeight /= 2

            }
            return Bitmap.createScaledBitmap(srcBitmap, srcWidth, srcHeight, true)
        }
    }

}

class AttachedImageDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}