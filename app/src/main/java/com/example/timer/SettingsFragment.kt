package com.example.timer

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import nl.invissvenska.numberpickerpreference.NumberDialogPreference
import nl.invissvenska.numberpickerpreference.NumberPickerPreferenceDialogFragment

class SettingsFragment : PreferenceFragmentCompat() {
    private val getMusic = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)
        with (prefs.edit()){
            if (uri != null) {
                putString("file", uri.toString())
                commit()
                context?.contentResolver?.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
        Log.i("music preference","music preference value was updated to: " + prefs.getString("file", ""))
    }

    private val getFolder = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)
        with (prefs.edit()){
            if (uri != null) {
                putString("folder", uri.toString())
                commit()
                context?.contentResolver?.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
        Log.i("folder preference","folder preference value was updated to: " + prefs.getString("folder", ""))
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        Log.i("preferences", "preferences reached")
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        //get music file picked by user
        if (preference.key == "file") {
            getMusic.launch(arrayOf("audio/mpeg"))
        }

        else if (preference.key == "folder") {
            getFolder.launch(null)
        }
        return super.onPreferenceTreeClick(preference)
    }

    private val DIALOG_FRAGMENT_TAG = "CustomPreferenceDialog"

    override fun onDisplayPreferenceDialog(preference: Preference) {
        if (preference is NumberDialogPreference) {
            val dialogPreference: NumberDialogPreference = preference
            val dialogFragment: DialogFragment = NumberPickerPreferenceDialogFragment
                .newInstance(
                    dialogPreference.key,
                    dialogPreference.minValue,
                    dialogPreference.maxValue,
                    dialogPreference.stepValue,
                    dialogPreference.unitText
                )
            dialogFragment.setTargetFragment(this, 0)
            dialogFragment.show(parentFragmentManager, DIALOG_FRAGMENT_TAG)
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }
}