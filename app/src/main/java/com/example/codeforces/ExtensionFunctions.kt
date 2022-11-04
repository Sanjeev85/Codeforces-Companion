package com.example.codeforces

import java.text.SimpleDateFormat
import java.util.*

fun convertTime(seconds: Int): String {
    val minutes = seconds / 60
    val hrs = minutes / 60
    val rem_min = if ((minutes - hrs * 60) == 0) "00" else (minutes - hrs * 60).toString()
    return hrs.toString() + " : " + rem_min
}

fun unixTimeToCurrTime(time: String): String {
    val simpleDateFor = SimpleDateFormat("dd/MM/yyyy")
    val currDate = Date(time.toLong() * 1000)
    return simpleDateFor.format(currDate)
}