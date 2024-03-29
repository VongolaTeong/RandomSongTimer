package com.example.timer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.documentfile.provider.DocumentFile
import androidx.preference.PreferenceManager
import java.io.*


//receiver to receive alarms
class AlarmReceiver : BroadcastReceiver() {
    private val notificationID = 100
    private val channelID = "1000"
    private val mediaPlayer = MediaPlayer()

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.extras?.getInt("action")
        Log.i("action", action.toString())

        when (action) {

            101 -> {
                //send alarm notification
                Log.i("alarm", "alarm sent")
                createChannel(context)
                createNotification(context)

                val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

                //if toggled, play random song from selected folder, else play selected song
                if (prefs.getBoolean("random", true)) {
                    val directoryPath = prefs.getString("folder", "")
                    Log.i("directory path", directoryPath.toString())
                    val uri = Uri.parse(directoryPath)
                    Log.i("uri", uri.toString())
                    val documentFile = DocumentFile.fromTreeUri(context, uri)
                    if (documentFile != null) {
                        val finalFile = listFiles(documentFile)

                        val path = fileFromContentUri(context, finalFile.uri)
                        Log.i("random song", path.absolutePath)
                        if (prefs.getBoolean("sound", false)) {
                            MusicControl.getInstance(context)?.playMusic(path.absolutePath)
                        }
                    }


                }
                else {
                    val stringPath = prefs.getString("file", "")
                    val uri:Uri = Uri.parse(stringPath)

                    //read file and play it in media player
                    val file = fileFromContentUri(context, uri)
                    Log.i("file path", file.absolutePath)

                    if (prefs.getBoolean("sound", false)) {
                        MusicControl.getInstance(context)?.playMusic(file.absolutePath)
                    }
                }

            }
            else -> {
                Log.i("media player state", mediaPlayer.toString())
                val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                if (prefs.getBoolean("sound", false)) {
                    MusicControl.getInstance(context)?.stopMusic()
                }
                val notificationManager: NotificationManager =
                    context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(notificationID)
                Log.i("dismiss", "dismiss the notification")
            }
        }
        }

    //recursive function to list files within directories
    private fun listFiles(documentFile: DocumentFile): DocumentFile {
        if (documentFile.isDirectory){
            val documentFiles = documentFile.listFiles()
            val randomIndex = (0..(documentFiles.size)).random()
            return listFiles(documentFiles[randomIndex])
        }

        else { return documentFile}
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
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 3, intent, 0)

        //intent to dismiss alarm
        val dismissIntent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("dismiss","dismiss")

        val dismissPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, 1, dismissIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        val builder = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_alarm_add)
            .setContentTitle(context.getString(R.string.channel_name))
            .setContentText(context.getString(R.string.alarm))
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_alarm_add, context.getString(R.string.dismiss),
                dismissPendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(context)) {
            notify(notificationID, builder.build())
        }
    }

    private fun fileFromContentUri(context: Context, contentUri: Uri): File {
        // Preparing Temp file name
        val fileExtension = getFileExtension(context, contentUri)
        val fileName = "temp_file" + if (fileExtension != null) ".$fileExtension" else ""

        // Creating Temp file
        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()

        try {
            val oStream = FileOutputStream(tempFile)
            val inputStream = context.contentResolver.openInputStream(contentUri)

            inputStream?.let {
                copy(inputStream, oStream)
            }

            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tempFile
    }

    private fun getFileExtension(context: Context, uri: Uri): String? {
        val fileType: String? = context.contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }
}


