package com.example.timer

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class MainActivity : AppCompatActivity() {
    //byte to record current state of the app
    private val initial: Byte = 0
    private val start: Byte = 1
    private val pause: Byte = 2
    private val finished: Byte = 3
    private var timerState: Byte = initial

    //input UI
    private lateinit var inputUI: LinearLayout

    //number pickers
    private lateinit var numberPickerSeconds: NumberPicker
    private lateinit var numberPickerMinutes: NumberPicker
    private lateinit var numberPickerHours: NumberPicker
    private lateinit var myTimer: CountDownTimer

    //buttons
    private lateinit var resumeButton: Button
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private lateinit var resetButton: Button
    private lateinit var settingButton: ImageButton

    //long to store time left when paused
    private var timeLeft:Long = 0

    //count down timer
    private lateinit var timerUI: LinearLayout
    private lateinit var timerHour: TextView
    private lateinit var timerMinute: TextView
    private lateinit var timerSecond: TextView

    //save the input time for reset
    private var inputTime:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //check run time permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED){
            Log.i("read permission", "read permission granted")
        }

        else {
            val isGranted = ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 111)
            if (isGranted.equals(false)){
                //no permission granted
                val toast = Toast.makeText(this, "No permission granted!", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

        setContentView(R.layout.activity_main)

        //find view by ID
        inputUI = findViewById(R.id.timer_input)
        resumeButton = findViewById(R.id.button_resume_timer)
        startButton = findViewById(R.id.button_start_timer)
        pauseButton = findViewById(R.id.button_pause_timer)
        resetButton = findViewById(R.id.button_reset_timer)
        settingButton = findViewById(R.id.button_setting)
        numberPickerSeconds = findViewById(R.id.number_picker_seconds)
        numberPickerMinutes = findViewById(R.id.number_picker_minutes)
        numberPickerHours = findViewById(R.id.number_picker_hours)
        timerUI = findViewById(R.id.count_down_timer)
        timerHour = findViewById(R.id.timer_hour)
        timerMinute = findViewById(R.id.timer_minute)
        timerSecond = findViewById(R.id.timer_second)

        //button listeners
        resumeButton.setOnClickListener{
            val toast = Toast.makeText(this, "Timer resumed", Toast.LENGTH_SHORT)
            toast.show()
            configResumeState()
            startAlarm(timeLeft)
            scheduleAlarm(timeLeft)
            //TODO
        }
        startButton.setOnClickListener{
            //catch the case of no input
            if (numberPickerSeconds.value==0 && numberPickerMinutes.value==0 && numberPickerHours.value==0) {
                val toast = Toast.makeText(this, "No input detected!", Toast.LENGTH_SHORT)
                toast.show()
            }
            else {
                //get the interval and start the timer
                val interval = getInterval()
                inputTime = interval
                timeLeft = interval
                startAlarm(interval)

                val toast = Toast.makeText(this, "Timer started", Toast.LENGTH_SHORT)
                toast.show()
                configStartState()

                //setup the alarm
                scheduleAlarm(interval)
            }

            //TODO
        }
        pauseButton.setOnClickListener{
            val toast = Toast.makeText(this, "Timer Paused", Toast.LENGTH_SHORT)
            toast.show()
            configPauseState()
            cancelAlarm()
            //TODO
        }
        resetButton.setOnClickListener{
            val toast = Toast.makeText(this, "Timer Reset", Toast.LENGTH_SHORT)
            toast.show()
            configInitialState()
            cancelAlarm()
            //TODO
        }
        settingButton.setOnClickListener{
            //TODO
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
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

    //function to display remaining time
    private fun displayTimeLeft(timeLeft: Long) {
        var seconds = timeLeft / 1000
        var minutes = seconds / 60
        val hours = minutes / 60
        seconds %= 60
        minutes %= 60
        timerHour.text = String.format("%02d", hours)
        timerMinute.text = String.format("%02d", minutes)
        timerSecond.text = String.format("%02d", seconds)
    }

    //functions to show different UI in different states
    private fun configInitialState() {
        inputUI.visibility = View.VISIBLE
        timerUI.visibility = View.GONE
        startButton.visibility = View.VISIBLE
        pauseButton.visibility = View.GONE
        resumeButton.visibility = View.GONE
        resetButton.visibility = View.GONE
        timerState = initial
        numberPickerSeconds.value = 0
        numberPickerMinutes.value = 0
        numberPickerHours.value = 0
        timeLeft = 0
    }

    private fun configStartState() {
        inputUI.visibility = View.GONE
        timerUI.visibility = View.VISIBLE
        startButton.visibility = View.GONE
        pauseButton.visibility = View.VISIBLE
        resumeButton.visibility = View.GONE
        resetButton.visibility = View.VISIBLE
        timerState = start
    }

    private fun configPauseState() {
        inputUI.visibility = View.GONE
        timerUI.visibility = View.VISIBLE
        startButton.visibility = View.GONE
        pauseButton.visibility = View.GONE
        resumeButton.visibility = View.VISIBLE
        resetButton.visibility = View.VISIBLE
        timerState = pause
        displayTimeLeft(timeLeft)
    }

    private fun configResumeState() {
        inputUI.visibility = View.GONE
        timerUI.visibility = View.VISIBLE
        startButton.visibility = View.GONE
        pauseButton.visibility = View.VISIBLE
        resumeButton.visibility = View.GONE
        resetButton.visibility = View.VISIBLE
        timerState = start

        startAlarm(timeLeft)
        displayTimeLeft(timeLeft)
    }

    private fun configFinishedState() {
        inputUI.visibility = View.VISIBLE
        timerUI.visibility = View.GONE
        startButton.visibility = View.VISIBLE
        pauseButton.visibility = View.GONE
        resumeButton.visibility = View.GONE
        resetButton.visibility = View.GONE
        timerState = finished
    }

    private fun startAlarm(interval: Long){
        myTimer = object: CountDownTimer(interval, 1000) {
            //to implement the pause function, check if the pause button is clicked on every tick
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                displayTimeLeft(millisUntilFinished)
                //if pause or reset button is clicked, save the time left and cancel the timer
                if (timerState==pause || timerState==initial){
                    myTimer.cancel()
                }
            }

            override fun onFinish() {
                Log.i("finish", "timer finished")
                val toast = Toast.makeText(this@MainActivity, "timer finished", Toast.LENGTH_SHORT)
                toast.show()
                configFinishedState()
            }
        }.start()
    }

    //function to schedule alarm
    private fun scheduleAlarm(interval: Long) {
        Log.i("call schedule alarm", "called")
        //alarm manager and intent
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("action",101)

        val pendingIntent:PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getBroadcast(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        //set the alarm
        Log.i("interval in schedule", interval.toString())
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().timeInMillis + interval, pendingIntent)
    }
    //function to cancel scheduled alarm
    private fun cancelAlarm() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("action",102)

        val pendingIntent:PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getBroadcast(
                this, 0,
                intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getBroadcast(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        //set the alarm
        alarmManager.cancel(pendingIntent)
    }
}