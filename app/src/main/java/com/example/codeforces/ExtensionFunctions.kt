package com.example.codeforces

import android.graphics.Color
import java.text.SimpleDateFormat
import java.util.*

fun convertTime(seconds: Int): String {
    val minutes = seconds / 60
    val hrs = minutes / 60
    val rem_min = if ((minutes - hrs * 60) == 0) "00" else (minutes - hrs * 60).toString()
    return hrs.toString() + " : " + rem_min
}

fun unixTimeToCurrTime(time: String): String {
    val simpleDateFor = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
    val currDate = Date(time.toLong() * 1000)
    return simpleDateFor.format(currDate)
}

fun getColorByRating(rating: Int): Int {
    if (rating <= 1199) return Color.parseColor("#808080")
    else if (rating <= 1399) return Color.parseColor("#008000")
    else if (rating <= 1599) return Color.parseColor("#03a89e")
    else if (rating <= 1899) return Color.parseColor("#0000ff")
    else if (rating <= 2099) return Color.parseColor("#800080")
    else if (rating <= 2299) return Color.parseColor("#fea500")
    else if (rating <= 2399) return Color.parseColor("#b17300")
    else if (rating <= 2599) return Color.parseColor("#ff0000")
    else if (rating <= 2999) return Color.parseColor("#ff0000")
    else return Color.parseColor("#ff0000")
}