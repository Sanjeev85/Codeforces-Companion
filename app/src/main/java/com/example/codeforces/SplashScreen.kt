package com.example.codeforces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import com.example.codeforces.databinding.ActivityContestHistoryBinding
import com.example.codeforces.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    lateinit var top: Animation
    lateinit var bottom: Animation
    lateinit var binding: ActivitySplashScreenBinding
    lateinit var slide_in: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        top = AnimationUtils.loadAnimation(this, R.anim.up)
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom)
        slide_in = AnimationUtils.loadAnimation(this, R.anim.slide_in)

        binding.idd.animation = top
        binding.tv1.animation = bottom
        binding.tv2.animation = slide_in

        Handler().postDelayed(object : Runnable {
            override fun run() {
                startActivity(Intent(this@SplashScreen, Login::class.java))
                finish()
            }
        }, 5000)


    }
}