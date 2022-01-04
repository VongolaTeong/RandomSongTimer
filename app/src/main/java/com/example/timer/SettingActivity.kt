package com.example.timer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)
        supportFragmentManager
            .beginTransaction()
            //.add(SettingsFragment(), "tag")
            .replace(R.id.settings, SettingsFragment())
            .commit()
    }
}