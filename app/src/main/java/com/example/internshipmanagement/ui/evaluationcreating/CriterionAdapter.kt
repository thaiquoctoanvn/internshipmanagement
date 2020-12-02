package com.example.internshipmanagement.ui.evaluationcreating

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.Criterion
import com.example.internshipmanagement.util.FunctionHelper
import kotlinx.android.synthetic.main.item_criterion.view.*

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
            ArrayAdapter(
                itemView.context,
                android.R.layout.simple_spinner_item,
                FunctionHelper.provideMarkLevel()
            ).also {
                it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                itemView.spItemCriterionMark.adapter = it
            }
            itemView.spItemCriterionMark.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        adapterView: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        criterion.mark = position.toString()
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>?) {

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