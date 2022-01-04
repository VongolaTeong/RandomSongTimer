package com.example.timer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


//receiver to receive alarms
class AlarmReceiver : BroadcastReceiver() {
    private val notificationID = 100
    private val channelID = "1000"

    override fun onReceive(context: Context, intent: Intent) {
        when {
            Intent.ACTION_BOOT_COMPLETED == intent.action -> {
                //when rebooted, reset all alarms
                //TODO
            }
            Intent.ACTION_DELETE == intent.action -> {
                Log.i("dismiss", "dismiss the notification")
                val notificationManager:NotificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(notificationID)
            }
            else -> {
                //send alarm notification
                //TODO
                Log.i("alarm", "alarm sent")
                createChannel(context)
                createNotification(context)
                //play song

            }
        }
    }

    //create notification channel
    private fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name:String = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(channelID, name, importance)
            mChannel.description = descriptionText

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager:NotificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun createNotification(context: Context) {
        //intent to open app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        //intent to dismiss alarm
        val dismissIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = Intent.ACTION_DELETE
            //TODO
            //cancel the music
        }
        val dismissPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 1, dismissIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        //set song
        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        //TODO: pick song from folder

        val builder = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_alarm_add)
            .setContentTitle(context.getString(R.string.channel_name))
            .setContentText(context.getString(R.string.alarm))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.ic_alarm_add, context.getString(R.string.dismiss),
                dismissPendingIntent)
            .setSound(alarmSound)
        with(NotificationManagerCompat.from(context)) {
            notify(notificationID, builder.build())
        }
    }
}
