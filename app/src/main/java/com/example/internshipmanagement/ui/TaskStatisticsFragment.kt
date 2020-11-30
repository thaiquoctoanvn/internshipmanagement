package com.example.internshipmanagement.ui

import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.internshipmanagement.R
import com.example.internshipmanagement.data.entity.CriterionPoint
import com.example.internshipmanagement.data.entity.TaskPoint
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
import kotlinx.android.synthetic.main.fragment_task_statistics.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class TaskStatisticsFragment : BaseFragment(), OnChartValueSelectedListener {

    private val userViewModel by sharedViewModel<UserViewModel>()

    override fun getRootLayoutId(): Int {
        return R.layout.fragment_task_statistics
    }

    override fun setViewOnEventListener() {

    }

    override fun setObserverFragment() {
        userViewModel.taskPoints.observe(viewLifecycleOwner, Observer {
            setChartData(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userViewModel.getTaskPoints()
    }

    private fun setUpPieChart() {
        pcTask.apply {

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

            setOnChartValueSelectedListener(this@TaskStatisticsFragment)
        }
    }

    private fun setChartData(taskPoints: MutableList<TaskPoint>) {

        pbTaskStatisticLoading.visibility = View.GONE

        if(taskPoints.size > 0) {
            tvNoTaskStatistic.visibility = View.GONE

            setUpPieChart()

            var goodTaskNumber = 0
            var mediumTaskNumber = 0
            var uncompletedTaskNumber = 0

            taskPoints.forEach {
                when {
                    it.isSubmitted == "1" && it.mark.toInt() >= 7 -> goodTaskNumber++
                    it.isSubmitted == "1" && it.mark.toInt() < 7 && it.mark.toInt() >= 5 -> mediumTaskNumber++
                    else -> uncompletedTaskNumber++
                }
            }

            val pieEntries = mutableListOf<PieEntry>(
                PieEntry(uncompletedTaskNumber.toFloat(), 0),
                PieEntry(mediumTaskNumber.toFloat(), 1),
                PieEntry(goodTaskNumber.toFloat(), 2)
            )

            val pieDataSet = PieDataSet(pieEntries, "Task Statistical").apply {
                setDrawIcons(false)

                // Khoảng cách giữa các phần
                sliceSpace = 3f

                // Độ dày phần được chọn
                selectionShift = 8f

                colors = mutableListOf(
                    ContextCompat.getColor(requireActivity(), R.color.not_completed_task_color),
                    ContextCompat.getColor(requireActivity(), R.color.medium_task_color),
                    ContextCompat.getColor(requireActivity(), R.color.good_task_color)
                )
            }

            val pieData = PieData(pieDataSet).apply {
                // Set dạng cho value hiển thị trên chart
                setValueFormatter(PercentFormatter())
                setValueTextSize(16f)
                setValueTypeface(Typeface.DEFAULT_BOLD)
                setValueTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }

            pcTask.apply {
                data = pieData
                legend.isEnabled = false
                highlightValue(null)
                invalidate()
            }

            val legends = mutableListOf(
                getString(R.string.uncompleted_task),
                getString(R.string.bad_task),
                getString(R.string.good_task)
            )
            FunctionHelper.generateCustomLegends(layoutTaskLegendContainer, pieDataSet, legends)

        } else {
            tvNoTaskStatistic.visibility = View.VISIBLE
        }

    }

    override fun onValueSelected(entry: Entry?, highlight: Highlight?) {
        pcTask.highlightValue(highlight)
    }

    override fun onNothingSelected() {

    }

    private fun generateCustomLegends(legend: Legend, pieDataSet: PieDataSet) {
        val colors = pieDataSet.colors
        val goodTaskLegend =
            LegendEntry(getString(R.string.bad_task), Legend.LegendForm.SQUARE, 20f, 8f, null, colors[0])
        val badTaskLegend =
            LegendEntry(getString(R.string.good_task), Legend.LegendForm.SQUARE, 20f, 8f, null, colors[1])
        legend.apply {
            // Vị trí của chú thích
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT

            // Chiều hiển thị của chú thích
            orientation = Legend.LegendOrientation.VERTICAL

            // Khoảng cách giữa các mục (vertical - y / horizontal - x)
            yEntrySpace = 28f

            isWordWrapEnabled = true

            // Khoảng cách của chú thích so với chart
            //yOffset = -50f

            setCustom(mutableListOf(badTaskLegend, goodTaskLegend))
        }
    }

}