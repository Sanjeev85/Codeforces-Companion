package com.example.codeforces.Screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Contactables
import android.util.Log
import com.example.codeforces.Fragments.ContestFragment
import com.example.codeforces.Fragments.SubmissionAnalytics
import com.example.codeforces.R
import kotlinx.android.synthetic.main.activity_generic.*

class genericActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generic)
        Log.e("GENERIC_ACTIVITY", "Entered")

        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.frame_layout, ContestFragment())
        ft.commit()
//        replaceFragment(ContestFragment())
//        btn1.setOnClickListener {
//            Log.e("REplace Frag", "Called")
        btn.setOnClickListener {
            replaceFragment(SubmissionAnalytics())
        }
        changeFrag.setOnClickListener {
            replaceFragment(ContestFragment())
        }

    }

    private fun replaceFragment(frag: ContestFragment) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.frame_layout, frag)
        ft.commit()
    }

    private fun replaceFragment(contestFragment: SubmissionAnalytics) {
        Log.e("Inside", "Replace Fragment")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, contestFragment)
        fragmentTransaction.commit()
    }
}