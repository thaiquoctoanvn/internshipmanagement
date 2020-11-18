package com.example.internshipmanagement.ui.adapter

import android.os.CountDownTimer
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.Criterion
import kotlinx.android.synthetic.main.item_criterion.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CriterionAdapter(
    private val onItemCriterionMarkChange: (mark: String) -> Unit
) : ListAdapter<Criterion, CriterionAdapter.CriterionViewHolder>(CriterionDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriterionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CriterionViewHolder(inflater.inflate(R.layout.item_criterion, parent, false))
    }

    override fun onBindViewHolder(holder: CriterionViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

    inner class CriterionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(criterion: Criterion) {
            itemView.tvItemCriterion.text = criterion.content
            itemView.etItemCriterionMark.doAfterTextChanged {
                if(!TextUtils.isEmpty(it.toString())) {
                    criterion.mark = it.toString().trim()
                }
            }
        }
    }
}

class CriterionDiffUtil : DiffUtil.ItemCallback<Criterion>() {
    override fun areItemsTheSame(oldItem: Criterion, newItem: Criterion): Boolean {
        return oldItem.content == newItem.content
    }

    override fun areContentsTheSame(oldItem: Criterion, newItem: Criterion): Boolean {
        return oldItem.mark == newItem.mark
    }

}