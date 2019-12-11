package com.example.audiolearning.util.timer

import androidx.lifecycle.LiveData
import kotlinx.coroutines.*

class Timer {

    private val _time =
        TimeMutableLiveData()
    val time: LiveData<String>
        get() = _time

    private var currentTimerJob: Job? = null
    private var isRunning: Boolean = false
    private var timeInMillis: Long = 0L

    fun start() {
        isRunning = true
        currentTimerJob = CoroutineScope(Dispatchers.Main).launch {
            while (isRunning) {
                delay(1000)
                timeInMillis += 1000
                _time.setValueFromMillis(timeInMillis)
            }
        }
    }

    fun stop() {
        isRunning = false
        timeInMillis = 0
        _time.setValueFromMillis(timeInMillis)

        if (currentTimerJob != null && currentTimerJob?.isActive!!) {
            currentTimerJob?.cancel()
        }
    }

    fun pause() {
        isRunning = false

        if (currentTimerJob != null && currentTimerJob?.isActive!!) {
            currentTimerJob?.cancel()
        }
    }

    fun resume() {
        start()
    }
}