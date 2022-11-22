package com.example.codeforces

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.example.codeforces.models.new_item
import kotlinx.android.synthetic.main.new_item.view.*

class adapter(private val contestList: ArrayList<new_item>) :
    RecyclerView.Adapter<adapter.FontestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontestViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.new_item, parent, false)
        return FontestViewHolder(view)
    }

    override fun onBindViewHolder(holder: FontestViewHolder, position: Int) {
        val currContest = contestList[position]
        holder.itemView.apply {
            contest_name.text = currContest.contest_name
            start_date.text = currContest.start_date
        }
    }


    override fun getItemCount(): Int {
        return contestList.size
    }

    inner class FontestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}