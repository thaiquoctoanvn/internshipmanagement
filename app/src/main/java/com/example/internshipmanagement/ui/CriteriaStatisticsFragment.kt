package com.example.internshipmanagement.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.CriterionPoint
import com.example.internshipmanagement.ui.base.BaseFragment
import com.example.internshipmanagement.util.FunctionHelper
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_criteria_statistics.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CriteriaStatisticsFragment : BaseFragment(), OnChartValueSelectedListener {

    private val userViewModel by sharedViewModel<UserViewModel>()

    override fun getRootLayoutId(): Int {
        return R.layout.fragment_criteria_statistics
    }

    override fun setViewOnEventListener() {

    }

    override fun setObserverFragment() {
        userViewModel.criteriaPoints.observe(viewLifecycleOwner, Observer {
            setChartData(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.getCriteriaPoints()
        Log.d("###", "Chart tab has loaded")
    }

    private fun setUpPieChart() {
        pcCriteria.apply {

            isDrawHoleEnabled = true
            holeRadius = 58f
            transparentCircleRadius = 100f
            rotationAngle = 0f
            isRotationEnabled = true
            isHighlightPerTapEnabled = true

            // Tắt chart decryption
            description.isEnabled = false

            // Set khoảng cách giữa chart (axist) so với viền, ko so với legend
            // nhưng bao gồm cả phần height của legend, cần set top or bottom
            // vừa đủ sao cho legend ko bị che đi
            extraTopOffset = 8f
            extraBottomOffset = 5f

//            setUsePercentValues(true)
            setDrawCenterText(true)

            // set màu phần lỗ tròn ở giữa
            setHoleColor(ContextCompat.getColor(requireActivity(), R.color.white))

            // Set màu và opacity phần giữa lỗ tròn và vòng màu chính
            setTransparentCircleColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.top_or_bottom_view_color
                )
            )
            setTransparentCircleAlpha(102)

            // Set tốc độ load chart và style load
            animateY(5000, Easing.EaseInOutQuad)

            setOnChartValueSelectedListener(this@CriteriaStatisticsFragment)
        }
    }

    private fun setChartData(criteriaPoints: MutableList<CriterionPoint>) {

        pbCriteriaStatisticLoading.visibility = View.GONE

        if(criteriaPoints.size > 0) {
            tvNoCriteriaStatistic.visibility = View.GONE

            setUpPieChart()

            // PieEntry('giá trị sẽ hiển thị trên chart', 'giá trị sẽ get khi click lên chart')
            var behaviorAverage = 0f
            var knowledgeAverage = 0f
            var proactiveAverage = 0f
            val counter = criteriaPoints.size

            criteriaPoints.forEach {
                behaviorAverage += it.behaviorMark.toFloat()
                knowledgeAverage += it.knowledgeMark.toFloat()
                proactiveAverage += it.proactiveMark.toFloat()
            }

            val pieEntries = mutableListOf<PieEntry>(
                PieEntry(behaviorAverage / counter, 0),
                PieEntry(knowledgeAverage / counter, 1),
                PieEntry(proactiveAverage / counter, 2)
            )

            val pieDataSet = PieDataSet(pieEntries, "Task Statistical").apply {
                setDrawIcons(false)

                // Khoảng cách giữa các phần
                sliceSpace = 3f

                // Độ dày phần được chọn
                selectionShift = 8f

                colors = mutableListOf(
                    ContextCompat.getColor(requireActivity(), R.color.behavior_color),
                    ContextCompat.getColor(requireActivity(), R.color.knowledge_color),
                    ContextCompat.getColor(requireActivity(), R.color.proactive_color)
                )
            }

            val pieData = PieData(pieDataSet).apply {
                // Set dạng cho value hiển thị trên chart
                setValueFormatter(PercentFormatter())
                setValueTextSize(16f)
                setValueTypeface(Typeface.DEFAULT_BOLD)
                setValueTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }

            pcCriteria.apply {
                data = pieData
                highlightValue(null)
                invalidate()
            }

            pcCriteria.legend.isEnabled = false
            FunctionHelper.generateCustomLegends(layoutCriteriaStatisticLegend, pieDataSet)
        } else {
            tvNoCriteriaStatistic.visibility = View.VISIBLE
        }

    }

    override fun onValueSelected(entry: Entry?, highlight: Highlight?) {
        pcCriteria.highlightValue(highlight)
    }

    override fun onNothingSelected() {

    }

    private fun generateCustomLegends(legend: Legend, pieDataSet: PieDataSet) {
        val colors = pieDataSet.colors
        val behaviorLegend =
            LegendEntry("Behavior Criterion", Legend.LegendForm.CIRCLE, 20f, 8f, null, colors[0])
        val knowledgeLegend =
            LegendEntry("Knowledge Criterion", Legend.LegendForm.CIRCLE, 20f, 8f, null, colors[1])
        val proactiveLegend =
            LegendEntry("Proactive Criterion", Legend.LegendForm.CIRCLE, 20f, 8f, null, colors[2])
        legend.apply {
            // Vị trí của chú thích
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT

            // Chiều hiển thị của chú thích
            orientation = Legend.LegendOrientation.HORIZONTAL

            // Khoảng cách giữa các mục (vertical - y / horizontal - x)
            xEntrySpace = 96f

            isWordWrapEnabled = true

            // Khoảng cách của chú thích so với chart
            //yOffset = -50f

            setCustom(mutableListOf(behaviorLegend, knowledgeLegend, proactiveLegend))
        }
    }

}