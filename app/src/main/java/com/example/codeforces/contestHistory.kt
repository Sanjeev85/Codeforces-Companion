package com.example.codeforces

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Color.*
import android.icu.text.UnicodeSet.EntryRange
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.marginTop
import com.example.codeforces.databinding.ActivityContestHistoryBinding
import com.example.codeforces.models.ResultXXX
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class contestHistory : AppCompatActivity() {
    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var gson: Gson

    lateinit var binding: ActivityContestHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContestHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        gson = Gson()



        val jsonList = sharedPref.getString("ratingChanges", "")
        Log.e("data", jsonList.toString())
        val itemType = object : TypeToken<List<ResultXXX>>() {}.type
        val ratingData = gson.fromJson<ArrayList<ResultXXX>>(jsonList, itemType)
        val yValues = arrayListOf<Entry>()
        val labels = arrayListOf<String>()
        for (i in ratingData.indices) {
            labels.add(unixTimeToCurrTime(ratingData[i].ratingUpdateTimeSeconds.toString()))
            yValues.add(Entry((i + 5).toFloat(), ratingData[i].newRating.toFloat()))
        }
        Log.e("lable", labels.toString())


        binding.ratingChangesForUser.text = "Rating Changes For ${ratingData[0].handle}"


        val set1 = LineDataSet(yValues, "Rating Changes")

        set1.fillAlpha = 110
        set1.valueTextSize = 14f
        set1.color = RED
        set1.isHighlightEnabled = true
        set1.lineWidth = 5f
        set1.valueTextColor = BLACK


        val x_axisLabel = binding.reportingChart.xAxis
        x_axisLabel.apply {
            position = XAxisPosition.BOTTOM
            setDrawGridLines(false)
            labelCount = ratingData.size
            labelRotationAngle = 90f
            valueFormatter = IndexAxisValueFormatter(labels)
        }

        val dataset = mutableListOf<ILineDataSet>()
        dataset.add(set1)

        val data = LineData(dataset)
        binding.reportingChart.data = data

    }

}