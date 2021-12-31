package com.example.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

//receiver to receive alarms
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            //when rebooted, reset all alarms
            //TODO
        } else {
            //send alarm notification
            //TODO
            Log.i("alarm", "alarm sent")
        }
    }
}
