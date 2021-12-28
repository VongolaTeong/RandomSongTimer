package com.example.timer

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val resumeButton: Button = findViewById(R.id.button_resume_timer)
        val startButton: Button = findViewById(R.id.button_start_timer)
        val stopButton: Button = findViewById(R.id.button_stop_timer)
        val resetButton: Button = findViewById(R.id.button_reset_timer)
        val settingButton: ImageButton = findViewById(R.id.button_setting)

        resumeButton.setOnClickListener{
            val toast = Toast.makeText(this, "Resume timer", Toast.LENGTH_SHORT)
            toast.show()
            //TODO
        }
        startButton.setOnClickListener{
            val toast = Toast.makeText(this, "Start timer", Toast.LENGTH_SHORT)
            toast.show()
            //TODO
        }
        stopButton.setOnClickListener{
            val toast = Toast.makeText(this, "Stop timer", Toast.LENGTH_SHORT)
            toast.show()
            //TODO
        }
        resetButton.setOnClickListener{
            val toast = Toast.makeText(this, "Reset timer", Toast.LENGTH_SHORT)
            toast.show()
            //TODO
        }
        settingButton.setOnClickListener{
            //TODO
        }
    }
}