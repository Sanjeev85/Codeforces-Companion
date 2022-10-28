package com.example.codeforces.Screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.codeforces.Fragments.ContestFragment
import com.example.codeforces.R
import com.example.codeforces.fragment_2
import kotlinx.android.synthetic.main.activity_generic.*

class genericActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generic)
        Log.e("GENERIC_ACTIVITY", "Entered")
        replaceFragment(ContestFragment())
//        btn1.setOnClickListener {
//            Log.e("REplace Frag", "Called")
//        }
//        btn2.setOnClickListener {
//            replaceFragment2(fragment_2())
//        }
    }

    private fun replaceFragment2(fragment2: fragment_2) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment2)
        fragmentTransaction.commit()

    }

    private fun replaceFragment(contestFragment: ContestFragment) {
        Log.e("Inside", "Replace Fragment")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, contestFragment)
        fragmentTransaction.commit()
    }
}