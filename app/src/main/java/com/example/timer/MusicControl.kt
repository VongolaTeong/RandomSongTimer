package com.example.timer

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer

class MusicControl(private val mContext: Context?) {
    private var mediaPlayer = MediaPlayer()
    fun playMusic(path: String) {
        mediaPlayer.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
        try {
            mediaPlayer.setDataSource(path)
            mediaPlayer.setOnPreparedListener { mp -> mp.start() }
            mediaPlayer.prepareAsync()
        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop()
            mediaPlayer.seekTo(0)
        }
    }

    companion object {
        private var sInstance: MusicControl? = null
        fun getInstance(context: Context?): MusicControl? {
            if (sInstance == null) {
                sInstance = MusicControl(context)
            }
            return sInstance
        }
    }
}