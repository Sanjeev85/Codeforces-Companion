package com.example.codeforces.Screens

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.codeforces.R
import com.example.codeforces.api.codeforcesApi
import com.example.codeforces.models.Contests
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_image.*
import java.io.IOException
import com.example.codeforces.models.Result
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main_screen.*
import kotlinx.android.synthetic.main.item_add.*
import kotlinx.coroutines.*
import retrofit2.*
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date

class mainScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()

        // fetch shared Prefence Image
        val json = sharedPref.getString("json", "")
        val obj = gson.fromJson<Result>(
            json, Result::class.java
        )
        Log.e("inside mainScreen", obj.toString())
        polulateResources(obj)

        //upComing Contests
        cc.setOnClickListener {
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
                    Log.e("CHAKRA", "UI")
                    dlg()
                    val api = codeforcesApi.create().getContestList().awaitResponse()
                    if (api.isSuccessful) {
                        for (i in 0..10) {
                            Log.e("Success ", api.body()!!.result[i].toString())
                        }
                    }

                }
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

    private fun unixTimeToCurrTime(time: String): String? {
        try {
            val simpleDateFor = SimpleDateFormat("MM/dd/yyyy")
            val currDate = Date(time.toLong() * 1000)
            return simpleDateFor.format(currDate)
        } catch (e: IOException) {
            return null
        }
    }

    private fun polulateResources(obj: Result) {

        // setImage Resource
        CoroutineScope(Dispatchers.IO).launch {
            val bm = getImageFromUrl(obj.avatar)
            withContext(Dispatchers.Main) {
                image.setImageBitmap(bm)
            }
        }

        // sethandle
        userHandle.apply {
            text = obj.handle
            setTextColor(Color.GREEN)
        }
        userRank.text = obj.rank
        userMaxRank.apply {
            text = "maxRank " + obj.maxRank
            setTextColor(Color.GREEN)
        }
        userMaxRating.text = "maxRating " + obj.maxRating.toString()
        userLastOnline.text =
            "lastOnline " + unixTimeToCurrTime(obj.lastOnlineTimeSeconds.toString())
    }
}