package com.example.codeforces

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import com.example.codeforces.Screens.genericActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_image.*
import java.io.IOException
import com.example.codeforces.models.Result
import com.example.codeforces.models.ResultX
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main_screen.*
import kotlinx.android.synthetic.main.item_add.*
import kotlinx.android.synthetic.main.item_image.tvUserHandle
import kotlinx.android.synthetic.main.item_image.userMaxRating
import kotlinx.android.synthetic.main.profile_demo.*
import kotlinx.coroutines.*
import retrofit2.*
import java.net.URL

class mainScreen : AppCompatActivity() {
    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var gson: Gson

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_demo)
        // init
        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        gson = Gson()


        val json = sharedPref.getString("json", "")
        val jsonHandle = sharedPref.getString("userHandle", "").toString()
        val obj = gson.fromJson<Result>(
            json, Result::class.java
        )
        populateCurrentPage(obj)

        // call all api's
        GlobalScope.launch {
            callEverything()
        }
        //upComing Contests
        upcoming_contest.setOnClickListener {
            val intent = Intent(this@mainScreen, contestList::class.java)
            startActivity(intent)
            finish()
        }

        ContestHistory.setOnClickListener {

        }

        submissions.setOnClickListener {
            val intent = Intent(this, Analytics::class.java)
            startActivity(intent)
            finish()
        }
    }

    private suspend fun callEverything() {
//        Log.e(TAG, "inside call everything")
        val status = getUpcomingContest()
        if (status == "UnSuccessful") {
            Log.e("Status", "Success upcoming Contest")
        }

    }

    private suspend fun getUpcomingContest(): String {
        Log.e(TAG, "Inside Upcoming Contest")
        val upComingContests = ArrayList<ResultX>()
        var flag = false
        withContext(Dispatchers.Main) {
            val api = codeforcesApi.create().getContestList().awaitResponse()
            if (api.isSuccessful) {
                flag = true
                val allContests = api.body()!!.result
                for (contest in allContests) {
                    if (contest.phase == "BEFORE") {
                        upComingContests.add(contest)
                    }
                }
                Log.e("contesst--", upComingContests.toString())
                val contestArray = gson.toJson(upComingContests)
                editor.apply {
                    putString("upcomingContest", contestArray)
                    apply()
                }
            }
        }
        if (flag) return "Successful"
        else return "UnSuccessful"
    }

    private fun dlg() {
        val dialog = Dialog(this@mainScreen)
        dialog.apply {
            setContentView(R.layout.animation)
            show()
        }
    }

    private fun getImageFromUrl(src: String): Bitmap? {
        try {
            val url = URL(src)
            val httpConn = url.openConnection()
            httpConn.apply {
                doInput = true
                connect()
            }
            val inputStream = httpConn.getInputStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            return bitmap
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("exception", e.message.toString())
            return null
        }
    }


    private fun populateCurrentPage(obj: Result) {

        // setImage Resource
        CoroutineScope(Dispatchers.IO).launch {
            val bm = getImageFromUrl(obj.avatar)
            withContext(Dispatchers.Main) {
                image.setImageBitmap(bm)
            }
        }

        tvUserHandle.apply {
            text = obj.handle
            setTextColor(getColorByRating(obj.rating))
        }

        tvUserHandle.setTextColor(getColorByRating(obj.maxRating))
        userMaxRating.text = buildString {
            append(obj.maxRank)
            append("( ")
            append(obj.maxRating)
            append(" )")
        }

        userMaxRating.setTextColor(getColorByRating(obj.rating))
    }

}