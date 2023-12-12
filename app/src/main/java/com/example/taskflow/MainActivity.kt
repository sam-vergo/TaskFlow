package com.example.taskflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.taskflow.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var dataHelper: DataHelper
    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        dataHelper = DataHelper(applicationContext)
        setContentView(binding.root)

       /*binding.home.setOnClickListener {
            replaceFragmentMain(MainActivity())
        }*/
        binding.tasks.setOnClickListener {
            replaceFragment(Calendar())
        }
        binding.shop.setOnClickListener {
            replaceFragment(Shop())
        }
        //setContentView(binding.root)
       // replaceFragment(Home())

/*
        /*navbar*/
        binding.bottomNavigationView.setOnItemReselectedListener { it ->
            when (it.itemId) {
                R.id.timer -> replaceFragment(Home())
                R.id.calendar -> replaceFragment(Calendar())
                R.id.statistics -> replaceFragment(Statistics())
                R.id.shop -> replaceFragment(Shop())

                else -> {

                }
            }
            true
        }
        */



        /*timer*/
        binding.toggleButton.setOnClickListener { startStopAction() }
        binding.resetButton.setOnClickListener { resetAction() }

        if (dataHelper.timerCounting()) {
            startTimer()
        } else {
            stopTimer()
        }

        timer.scheduleAtFixedRate(TimeTask(), 0, 500)
    }


    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.commit()
    }

    private inner class TimeTask : TimerTask() {
        override fun run() {
            if (dataHelper.timerCounting()) {
                val time = Date().time - dataHelper.startTime()!!.time
                binding.timeTV.text = timeStringFromLong(time)
            }
        }
    }

    private fun resetAction() {
        dataHelper.setStopTime(null)
        dataHelper.setStartTime(null)
        stopTimer()
        binding.timeTV.text = timeStringFromLong(0)
    }

    private fun stopTimer() {
        dataHelper.setTimerCounting(false)
        binding.toggleButton.text = getString(R.string.start)
    }

    private fun startTimer() {
        dataHelper.setTimerCounting(true)
        binding.toggleButton.text = getString(R.string.stop)
    }

    private fun startStopAction() {
        if (dataHelper.timerCounting()) {
            dataHelper.setStopTime(Date())
            stopTimer()
        } else {
            if (dataHelper.stopTime() != null) {
                dataHelper.setStartTime(calcRestartTime())
                dataHelper.setStopTime(null)
            } else {
                dataHelper.setStartTime(Date())
            }
            startTimer()
        }
    }

    private fun calcRestartTime(): Date {
        val diff = dataHelper.startTime()!!.time - dataHelper.stopTime()!!.time
        return Date(System.currentTimeMillis() + diff)
    }

    private fun timeStringFromLong(ms: Long): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60) % 60)
        val hours = (ms / (1000 * 60 * 60) % 24)
        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}