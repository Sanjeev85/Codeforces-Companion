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
import com.example.codeforces.models.ResultXXX
import com.example.codeforces.models.ratingChanges
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main_screen.*
import kotlinx.android.synthetic.main.item_add.*
import kotlinx.android.synthetic.main.item_image.tvUserHandle
import kotlinx.android.synthetic.main.item_image.userMaxRating
import kotlinx.android.synthetic.main.profile_demo.*
import kotlinx.coroutines.*
import retrofit2.*
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class mainScreen : AppCompatActivity() {
    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var gson: Gson
    lateinit var jsonHandle: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_demo)
        // init
        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        gson = Gson()


        val json = sharedPref.getString("json", "")
        jsonHandle = sharedPref.getString("userHandle", "").toString()
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
//            finish()
        }

        ContestHistory.setOnClickListener {
            val intent = Intent(this@mainScreen, contestHistory::class.java)
            startActivity(intent)
//            finish()
        }

        submissions.setOnClickListener {
            val intent = Intent(this, Analytics::class.java)
            startActivity(intent)
//            finish()
        }
    }

    private suspend fun callEverything() {
//        Log.e(TAG, "inside call everything")
        val status = fetchAllData()
//        if (status == "UnSuccessful") {
//            Log.e("Status", "Success upcoming Contest")
//        }

    }

    private suspend fun fetchAllData() {
        Log.e(TAG, "Inside Upcoming Contest")
        val upComingContests = ArrayList<ResultX>()

        // * Upcoming Contest RecyclerView Data Fetch
        withContext(Dispatchers.Main) {
            val api = codeforcesApi.create().getContestList().awaitResponse()
            if (api.isSuccessful) {
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

        // * Rating Changes Fetch FOr LineChart

        val ratings = arrayListOf<ResultXXX>()
        withContext(Dispatchers.IO) {
            val api = codeforcesApi.create().getRatingChanges(jsonHandle).awaitResponse()
            if (api.isSuccessful) {
                val allRatings = api.body()!!.result
                for (rating in allRatings) {
                    ratings.add(rating)
                }

                val ratingChangesArray = gson.toJson(ratings)
                editor.apply {
                    putString("ratingChanges", ratingChangesArray)
                    apply()
                }
            }
        }

        // * BarCHART FETCH START
        withContext(Dispatchers.IO) {
            val api = codeforcesApi.create().getSolvedProblems(jsonHandle).awaitResponse()
            val hashMap = TreeMap<String, Int>()
            for (i in api.body()!!.result.indices) {
                val tags_ = api.body()!!.result[i].problem.tags
                for (ele in tags_) {
                    val prev = hashMap.getOrDefault(ele, 0)
                    hashMap[ele] = prev + 1
                }
            }
            val ratingWiseCount = TreeMap<Int, Int>()

            for (i in api.body()!!.result.indices) {
                val currSubmission = api.body()!!.result[i]
                if (currSubmission.verdict == "OK") {
                    val problemRating = currSubmission.problem.rating
                    val curr = ratingWiseCount.getOrDefault(problemRating, 0)
                    ratingWiseCount[problemRating] = curr + 1
                }
            }

            val problemRatingCount = gson.toJson(ratingWiseCount)
            editor.apply {
                putString("BarChartData", problemRatingCount)
                apply()
            }
            // * Pie Chart Data Extract From Above BarChart
            var sum = 0
            for (value in hashMap.values) {
                sum += value
            }

            val pie = gson.toJson(hashMap)
            editor.apply {
                putString("PieChartData", pie)
                putString("sum_val", sum.toString())
                apply()
            }
        }
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