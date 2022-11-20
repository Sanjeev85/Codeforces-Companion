package com.example.codeforces

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codeforces.models.ResultX
import com.example.codeforces.models.new_item
import com.example.codeforces.models.singleContest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_contest_list.*

class contestList : AppCompatActivity() {
    lateinit var sharedPref: SharedPreferences
    lateinit var gson: Gson
    lateinit var contestList: ArrayList<new_item>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contest_list)

        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        gson = Gson()


        val contestJson = sharedPref.getString("upcomingContest", "")
        val itemType = object : TypeToken<List<ResultX>>() {}.type
        val lst = gson.fromJson<ArrayList<ResultX>>(
            contestJson, itemType
        )
//        Log.e("From Fragment", lst.toString())
        contestList = arrayListOf()
        for (contest in lst) {
            contestList.add(
                new_item(
                    contest.name,
                    unixTimeToCurrTime(contest.startTimeSeconds.toString())
                )
            )
        }

        val adapter = adapter(contestList)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)

    }
}