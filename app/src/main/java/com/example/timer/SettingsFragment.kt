package com.example.timer

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager

class SettingsFragment : PreferenceFragmentCompat() {
    private val getMusic = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)
        with (prefs.edit()){
            if (uri != null) {
                Log.i("content path", uri.toString())
                Log.i("path", uri.path.toString())
                putString("file", uri.toString())
                commit()
                context?.contentResolver?.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
        Log.i("music preference","music preference value was updated to: " + prefs.getString("file", ""))
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        Log.i("preferences", "preferences reached")
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        //get music file picked by user
        if (preference.key == "file") {
            //requestPermissionLauncher.launch(bool:Boolean)
            getMusic.launch(arrayOf("audio/mpeg"))
        }
        return super.onPreferenceTreeClick(preference)
    }
}