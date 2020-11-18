package com.example.internshipmanagement.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.MenteesEvaluation
import com.example.internshipmanagement.util.FunctionHelper
import com.example.internshipmanagement.util.SERVER_URL
import kotlinx.android.synthetic.main.item_mentees_evaluation.view.*

class EvaluationProfileAdapter : ListAdapter<MenteesEvaluation, EvaluationProfileAdapter.EvaluationProfileViewHolder>(EvaluationProfileDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EvaluationProfileViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EvaluationProfileViewHolder((inflater.inflate(R.layout.item_mentees_evaluation, parent, false)))
    }

    override fun onBindViewHolder(holder: EvaluationProfileViewHolder, position: Int) {
        holder.binData(getItem(position))
    }

    inner class EvaluationProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun binData(menteesEvaluation: MenteesEvaluation) {
            val fromDate = FunctionHelper.getDateFromTimeMilliSecond(menteesEvaluation.fromDate)
            val toDate = FunctionHelper.getDateFromTimeMilliSecond(menteesEvaluation.toDate)
            val period = "$fromDate - $toDate"
            val mentorName = "Mentor ${menteesEvaluation.mentorName}"
            itemView.tvItemEvaluationMentorName.text = mentorName
            itemView.tvItemEvaluationPeriod.text = period
            itemView.tvItemEvaluationContent.text = menteesEvaluation.evaluation
            Glide.with(itemView)
                .load("$SERVER_URL${menteesEvaluation.mentorAvatarUrl}")
                .placeholder(R.drawable.default_avatar)
                .circleCrop()
                .into(itemView.ivItemEvaluation)
        }
    }

}

class EvaluationProfileDiffUtil : DiffUtil.ItemCallback<MenteesEvaluation>() {
    override fun areItemsTheSame(oldItem: MenteesEvaluation, newItem: MenteesEvaluation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: MenteesEvaluation,
        newItem: MenteesEvaluation
    ): Boolean {
        return oldItem.evaluation == newItem.evaluation
    }
}