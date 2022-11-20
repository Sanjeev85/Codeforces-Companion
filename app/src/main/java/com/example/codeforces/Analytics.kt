package com.example.codeforces

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_analytics.*
import kotlinx.android.synthetic.main.activity_login.*

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Analytics : AppCompatActivity() {
    lateinit var sharedPref: SharedPreferences
    lateinit var editor: Editor
    lateinit var gson: Gson
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)

        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        gson = Gson()


        val handle = sharedPref.getString("userHandle", "")
        (problemTagstv).text = "Problem Tags Of $handle"
        (problemRatingtv).text = "Problem Rating Of $handle"


        val itemType = object : TypeToken<TreeMap<Int, Int>>() {}.type
        val ratingWiseCount = sharedPref.getString("BarChartData", "")
        val rv = gson.fromJson<TreeMap<Int, Int>>(ratingWiseCount, itemType)

        val itemType1 = object : TypeToken<TreeMap<String, Int>>(){}.type
        val barchartdata = sharedPref.getString("PieChartData", "")
        val data = gson.fromJson<TreeMap<String, Int>>(barchartdata, itemType1)
        val sum = sharedPref.getString("sum_val", "")?.toInt()

        showBarChart(rv)
        showPieChart(data, sum)
    }

    private fun showBarChart(problemRatingMap: TreeMap<Int, Int>) {
        val data = ArrayList<BarEntry>().toMutableList()
        val labels = ArrayList<String>()
        labels.add("JingleBell")

        var currIdx = 1
        for (i in 800..2500 step 100) {
            val curr_val = problemRatingMap.getOrDefault(i, 0)
            labels.add(i.toString())
            data.add(BarEntry(currIdx.toFloat(), curr_val!!.toFloat()))
            currIdx++
        }


        val barChart = findViewById<BarChart>(R.id.barchart)
        val x_axisLabel = barChart?.xAxis
        x_axisLabel?.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawGridLines(false)
            //labels
            labelCount = labels.size
            granularity = 1f
            labelRotationAngle = 270f
            valueFormatter = IndexAxisValueFormatter(labels)
        }


        val barchartDataset = BarDataSet(data, "Ratings of ${handle}")
        val barData = BarData(barchartDataset)
        barChart?.data = barData
        barchartDataset.colors = ColorTemplate.COLORFUL_COLORS.toMutableList()

        // * textColor
        barchartDataset.valueTextColor = Color.BLACK

        // * text size
        barchartDataset.valueTextSize = 16f
        barChart?.description?.isEnabled = true
    }

    private fun showPieChart(map: TreeMap<String, Int>, total: Int?) {
        val tagsAmountMap = HashMap<String, Float>()
        for ((key, value) in map) {
            var currContribution = (value.toFloat() / total!!.toFloat()) * 100f
            tagsAmountMap[key] = currContribution
        }
        val pieEntry = ArrayList<PieEntry>()
        val label = "Tags of $handle"

        for ((key, value) in tagsAmountMap) {
            pieEntry.add(PieEntry(value, key))
        }

        val pieDataSet = PieDataSet(pieEntry, label)
        pieDataSet.valueTextSize = 10f
        pieDataSet.colors = ColorTemplate.COLORFUL_COLORS.toMutableList()

        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(true)

        val pieChart = findViewById<PieChart>(R.id.pie)
        pieChart?.setData(pieData)
        pieChart?.invalidate()
    }
}

