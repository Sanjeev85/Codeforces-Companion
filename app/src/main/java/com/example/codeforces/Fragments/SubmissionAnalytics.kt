package com.example.codeforces.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import com.example.codeforces.R
import com.example.codeforces.api.codeforcesApi
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import java.util.TreeMap


class SubmissionAnalytics : Fragment(R.layout.fragment_submission_analytics) {
    lateinit var sharedPref: SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = this.requireContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE)

        val handle = sharedPref.getString("userHandle", "")
        view.findViewById<TextView>(R.id.problemTagstv).text = "Problem Tags Of $handle"
        view.findViewById<TextView>(R.id.problemRatingtv).text = "Problem Rating Of $handle"

        GlobalScope.launch {
            fetchProblems(handle)
        }
    }

    suspend fun fetchProblems(handle: String?) {
        val hashMap = TreeMap<String, Int>()
        var sum = 0
        withContext(Dispatchers.IO) {

            val api = codeforcesApi.create().getSolvedProblems(handle).awaitResponse()

            // * PieChart Data

            for (i in api.body()!!.result.indices) {
                val tags_ = api.body()!!.result[i].problem.tags
                for (ele in tags_) {
                    val prev = hashMap.getOrDefault(ele, 0)
                    hashMap[ele] = prev + 1
                }
            }

            // * barChart Data

            val ratingWiseCount = TreeMap<Int, Int>()

            for (i in api.body()!!.result.indices) {
                val currSubmission = api.body()!!.result[i]
                if (currSubmission.verdict == "OK") {
                    val problemRating = currSubmission.problem.rating
                    val curr = ratingWiseCount.getOrDefault(problemRating, 0)
                    ratingWiseCount[problemRating] = curr + 1
                }
            }
            showBarChart(ratingWiseCount)
        }

        for (value in hashMap.values) {
            sum += value
        }
////        Log.e(TAG, "sum = ${sum}")

        showPieChart(hashMap, sum)

    }

    private fun showBarChart(problemRatingMap: TreeMap<Int, Int>) {
//        Log.e("", "BarChart Inside")
        val data = ArrayList<BarEntry>().toMutableList()
        val labels = ArrayList<String>()
        labels.add("JingleBell")

        var currIdx = 1
        for (i in 800..2500 step 100) {
            val curr_val = problemRatingMap.getOrDefault(i, 0)
            labels.add(i.toString())
            data.add(BarEntry(currIdx.toFloat(), curr_val!!.toFloat()))
//            Log.e(" curr $i ", " map ${problemRatingMap.get(i)}")
            currIdx ++
        }
        Log.e("map", problemRatingMap.toString())
        Log.e("ArrayList", " = ${data}")


        val barChart = view?.findViewById<BarChart>(R.id.barchart)
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

    private fun showPieChart(map: TreeMap<String, Int>, total: Int) {
        val tagsAmountMap = HashMap<String, Float>()
        for ((key, value) in map) {
            var currContribution = (value.toFloat() / total.toFloat()) * 100f
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

        val pieChart = view?.findViewById<PieChart>(R.id.pie)
        pieChart?.setData(pieData)
        pieChart?.invalidate()
    }

}