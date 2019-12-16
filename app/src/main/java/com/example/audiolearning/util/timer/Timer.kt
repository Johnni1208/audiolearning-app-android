package com.example.audiolearning.util.timer

import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import java.lang.IllegalStateException

class Timer {

    private val _time =
        TimeMutableLiveData()
    val time: LiveData<String>
        get() = _time

    private var currentTimerJob: Job? = null
    private var isRunning: Boolean = false
    private var isPausing: Boolean = false
    private var timeInMillis: Long = 0L

    fun start() {
        if (currentTimerJob != null && currentTimerJob?.isActive!!) {
            throw IllegalStateException("Timer already running.")
        }

        isRunning = true
        currentTimerJob = CoroutineScope(Dispatchers.Main).launch {
            while (isRunning && !isPausing) {
                delay(1000)
                timeInMillis += 1000
                _time.setValueFromMillis(timeInMillis)
            }
        }
    }

    fun stop() {
        check(isRunning) { "Timer has not been started." }

        isRunning = false
        isPausing = false
        timeInMillis = 0
        _time.setValueFromMillis(timeInMillis)

        if (currentTimerJob != null && currentTimerJob?.isActive!!) {
            currentTimerJob?.cancel()
        }
    }

    fun pause() {
        check(isRunning) { "Timer has not been started." }
        check(!isPausing) { "Timer already paused." }
        isPausing = true

        if (currentTimerJob != null && currentTimerJob?.isActive!!) {
            currentTimerJob?.cancel()
        }
    }

    fun resume() {
        check(isRunning) { "Timer has not been started." }
        check(isPausing) { "Timer has not been paused." }
        isPausing = false
        start()
    }
}