package com.example.groshikiapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.groshikiapp.R
import com.example.groshikiapp.getCurrentUserName
import com.example.groshikiapp.getProfilePreferences

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val tw: TextView = findViewById(R.id.text)
        val sb = StringBuilder().apply{
            append("Profile: ")
            append(getProfilePreferences(applicationContext)?.name).append("\n")
            append("UserName: ")
            append(getCurrentUserName(applicationContext))
        }
        tw.text = sb.toString()
    }
}