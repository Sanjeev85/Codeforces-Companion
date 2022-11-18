package com.example.codeforces.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.codeforces.R
import com.example.codeforces.models.singleContest
import kotlinx.android.synthetic.main.single_contest.view.*


class contestAdapter(private val contestList: ArrayList<singleContest>) :
    RecyclerView.Adapter<contestAdapter.contestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): contestViewHolder {
        Log.e("adapter 1", "third time")
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_contest, parent, false)
        return contestViewHolder(view)
    }

    override fun onBindViewHolder(holder: contestViewHolder, position: Int) {
        val currContest = contestList[position]
        holder.itemView.apply {
            textView1.text = currContest.item2
            textView2.text = currContest.item1
            textView3.text = currContest.item3
            alarm.setOnClickListener {

            }
        }
    }

    override fun getItemCount(): Int {
        return contestList.size
    }

    inner class contestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}