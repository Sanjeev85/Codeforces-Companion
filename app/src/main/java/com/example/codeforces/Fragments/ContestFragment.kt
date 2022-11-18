package com.example.codeforces.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codeforces.R
import com.example.codeforces.adapters.contestAdapter
import com.example.codeforces.convertTime
import com.example.codeforces.models.ResultX
import com.example.codeforces.models.singleContest
import com.example.codeforces.unixTimeToCurrTime
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_contest.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.min


class ContestFragment : Fragment(R.layout.fragment_contest) {
    lateinit var sharedPref: SharedPreferences
    lateinit var gson: Gson
    lateinit var contestList: ArrayList<singleContest>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        sharedPref = this.requireContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        gson = Gson()


        val contestJson = sharedPref.getString("upcomingContest", "")
        val itemType = object : TypeToken<List<ResultX>>() {}.type
        val lst = gson.fromJson<ArrayList<ResultX>>(
            contestJson, itemType
        )
        Log.e("From Fragment", lst.toString())
        contestList = arrayListOf()
        for (contest in lst) {
            contestList.add(
                singleContest(
                    contest.name,
                    convertTime(contest.durationSeconds),
                    unixTimeToCurrTime(contest.startTimeSeconds.toString())
                )
            )
        }

        val adapter = contestAdapter(contestList)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(view.context)


    }


}