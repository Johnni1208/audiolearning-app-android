package com.audiolearning.app.util.timer

import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * This class provides a Timer which counts up.
 * Only usable with Android LiveData.
 */

class Timer @Inject constructor() {
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

        resetTimer()

        if (currentTimerJob != null && currentTimerJob?.isActive!!) {
            currentTimerJob?.cancel()
        }
    }

    private fun resetTimer(){
        isRunning = false
        isPausing = false
        timeInMillis = 0
        _time.setValueFromMillis(timeInMillis)
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