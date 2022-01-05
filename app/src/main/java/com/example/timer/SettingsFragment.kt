package com.example.timer

import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        Log.i("preferences", "preferences reached")
        setPreferencesFromResource(R.xml.preferences, rootKey)

        //val prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
    }
}