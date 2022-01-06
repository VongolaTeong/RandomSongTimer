package com.example.timer

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()

        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        val listener: SharedPreferences.OnSharedPreferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == "sound") {
                    Log.i("sound preference","Sound preference value was updated to: " + sharedPreferences.getBoolean("sound", true))
                }
            }

        prefs.registerOnSharedPreferenceChangeListener(listener)
        }
}