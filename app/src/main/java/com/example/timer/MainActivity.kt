package com.example.timer

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val initial: Byte = 0
    private val start: Byte = 1
    private val pause: Byte = 2
    private val finished: Byte = 3

    private var timerState: Byte = initial

    private lateinit var numberPickerSeconds: NumberPicker
    private lateinit var numberPickerMinutes: NumberPicker
    private lateinit var numberPickerHours: NumberPicker
    private lateinit var myTimer: CountDownTimer

    private lateinit var resumeButton: Button
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private lateinit var resetButton: Button
    private lateinit var settingButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //find view by ID
        resumeButton = findViewById(R.id.button_resume_timer)
        startButton = findViewById(R.id.button_start_timer)
        pauseButton = findViewById(R.id.button_pause_timer)
        resetButton = findViewById(R.id.button_reset_timer)
        settingButton = findViewById(R.id.button_setting)
        numberPickerSeconds = findViewById(R.id.number_picker_seconds)
        numberPickerMinutes = findViewById(R.id.number_picker_minutes)
        numberPickerHours = findViewById(R.id.number_picker_hours)

        //button listener
        resumeButton.setOnClickListener{
            val toast = Toast.makeText(this, "Timer resumed", Toast.LENGTH_SHORT)
            toast.show()
            configStartState()
            //TODO
        }
        startButton.setOnClickListener{
            //catch the case of no input
            if (numberPickerSeconds.value==0 && numberPickerMinutes.value==0 && numberPickerHours.value==0) {
                val toast = Toast.makeText(this, "No input detected!", Toast.LENGTH_SHORT)
                toast.show()
            }
            else {
                val interval = getInterval()
                myTimer = object: CountDownTimer(interval, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        //TODO
                    }

                    override fun onFinish() {
                        Log.i("finish", "timer finished")
                        val toast = Toast.makeText(this@MainActivity, "timer finished", Toast.LENGTH_SHORT)
                        toast.show()
                        //TODO
                    }
                }.start()
                val toast = Toast.makeText(this, "Timer started", Toast.LENGTH_SHORT)
                toast.show()
                configStartState()
            }

            //TODO
        }
        pauseButton.setOnClickListener{
            val toast = Toast.makeText(this, "Timer Paused", Toast.LENGTH_SHORT)
            toast.show()
            configPauseState()
            //TODO
        }
        resetButton.setOnClickListener{
            val toast = Toast.makeText(this, "Timer Reset", Toast.LENGTH_SHORT)
            toast.show()
            configInitialState()
            //TODO
        }
        settingButton.setOnClickListener{
            //TODO
        }

        setInputNumbersValues()
        configInitialState()
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

    //function to calculate timer interval
    private fun getInterval(): Long {
        val interval = ((numberPickerSeconds.value + numberPickerMinutes.value*60 + numberPickerHours.value*3600)*1000).toLong()
        Log.i("interval", interval.toString())
        return interval
    }

    //functions to show different UI in different states
    private fun configInitialState() {
        startButton.visibility = View.VISIBLE
        pauseButton.visibility = View.GONE
        resumeButton.visibility = View.GONE
        resetButton.visibility = View.GONE
        timerState = initial
        numberPickerSeconds.value = 0
        numberPickerMinutes.value = 0
        numberPickerHours.value = 0
    }

    private fun configStartState() {
        startButton.visibility = View.GONE
        pauseButton.visibility = View.VISIBLE
        resumeButton.visibility = View.GONE
        resetButton.visibility = View.VISIBLE
        timerState = start
    }

    private fun configPauseState() {
        startButton.visibility = View.GONE
        pauseButton.visibility = View.GONE
        resumeButton.visibility = View.VISIBLE
        resetButton.visibility = View.VISIBLE
        timerState = pause
    }
}