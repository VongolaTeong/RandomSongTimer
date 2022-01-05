package com.example.timer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        Log.i("preferences", "preferences reached")
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference.key == "file") {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "audio/mpeg"
            startActivityForResult(intent, 1)
        }
        return super.onPreferenceTreeClick(preference)
    }
}