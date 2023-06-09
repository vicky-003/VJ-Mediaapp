package com.example.social_mediavj

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

class DarkModeActivity : AppCompatActivity() {

    private lateinit var darkModeSwitch: SwitchCompat
    private var nightMode: Boolean = false
    private var editor: SharedPreferences.Editor? = null
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dark_mode)

        darkModeSwitch = findViewById(R.id.dark_Mode_Switch)

       //sharedPreferences = getSharedPreferences("MODE", MODE_PRIVATE)
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE)

        nightMode=sharedPreferences?.getBoolean("night", false)!!

        if (nightMode) {

            darkModeSwitch.isChecked = true

            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        darkModeSwitch.setOnCheckedChangeListener { compoundButton, state ->
            if (nightMode) {

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                editor=sharedPreferences?.edit()
                editor?.putBoolean("night", false)
            } else {

                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                editor=sharedPreferences?.edit()
                editor?.putBoolean("night", true)
            }
            editor?.apply()
        }
    }
}