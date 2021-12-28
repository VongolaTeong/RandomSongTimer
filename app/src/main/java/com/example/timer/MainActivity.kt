package com.example.timer

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var numberPickerSeconds: NumberPicker
    lateinit var numberPickerMinutes: NumberPicker
    lateinit var numberPickerHours: NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //find view by ID
        val resumeButton: Button = findViewById(R.id.button_resume_timer)
        val startButton: Button = findViewById(R.id.button_start_timer)
        val stopButton: Button = findViewById(R.id.button_stop_timer)
        val resetButton: Button = findViewById(R.id.button_reset_timer)
        val settingButton: ImageButton = findViewById(R.id.button_setting)
        numberPickerSeconds = findViewById(R.id.number_picker_seconds)
        numberPickerMinutes = findViewById(R.id.number_picker_minutes)
        numberPickerHours = findViewById(R.id.number_picker_hours)

        //button listener
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

        setInputNumbersValues()

    }
    //function to get input on time
    private fun setInputNumbersValues() {
        val secondsNumbers = arrayOfNulls<String>(61)
        for (i in secondsNumbers.indices) secondsNumbers[i] = String.format("%02d", i)

        val hoursNumbers = arrayOfNulls<String>(100)
        for (i in hoursNumbers.indices) hoursNumbers[i] = String.format("%02d", i)

        numberPickerSeconds.maxValue = 60
        numberPickerSeconds.minValue = 0
        numberPickerSeconds.displayedValues = secondsNumbers

        numberPickerMinutes.maxValue = 60
        numberPickerMinutes.minValue = 0
        numberPickerMinutes.displayedValues = secondsNumbers

        numberPickerHours.maxValue = 99
        numberPickerHours.minValue = 0
        numberPickerHours.displayedValues = hoursNumbers
    }
}