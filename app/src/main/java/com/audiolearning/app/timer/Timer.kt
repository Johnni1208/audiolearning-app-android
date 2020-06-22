package com.audiolearning.app.timer

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This class provides a Timer which counts up.
 * Only usable with Android LiveData.
 */
class Timer @Inject constructor() {
    companion object {
        private const val ERROR_TIMER_NOT_BEEN_STARTED = "Timer has not been started."
        private const val ERROR_TIMER_ALREADY_STARTED = "Timer already started."
        private const val ERROR_TIMER_ALREADY_PAUSED = "Timer already paused."
        private const val ERROR_TIMER_NOT_BEEN_PAUSED = "Timer has not been paused."
    }

    private val _time =
        TimeMutableLiveData()
    val time: LiveData<String>
        get() = _time

    private var currentTimerJob: Job? = null
    private var isRunning: Boolean = false
    private var isPausing: Boolean = false
    private var timeInMillis: Long = 0L
    private val timeBetweenIntervals = 1000L

    fun start() {
        if (currentTimerJob != null && currentTimerJob?.isActive!!) {
            throw IllegalStateException(ERROR_TIMER_ALREADY_STARTED)
        }

        isRunning = true
        currentTimerJob = CoroutineScope(Dispatchers.Main).launch {
            while (isRunning && !isPausing) {
                delay(timeBetweenIntervals)
                timeInMillis += timeBetweenIntervals
                _time.setValueFromMillis(timeInMillis)
            }
        }
    }

    fun stop() {
        check(isRunning) { ERROR_TIMER_NOT_BEEN_STARTED }

        resetTimer()

        if (currentTimerJob != null && currentTimerJob?.isActive!!) {
            currentTimerJob?.cancel()
        }
    }

    private fun resetTimer() {
        isRunning = false
        isPausing = false
        timeInMillis = 0
        _time.setValueFromMillis(timeInMillis)
    }

    fun pause() {
        check(isRunning) { ERROR_TIMER_NOT_BEEN_STARTED }
        check(!isPausing) { ERROR_TIMER_ALREADY_PAUSED }
        isPausing = true

        if (currentTimerJob != null && currentTimerJob?.isActive!!) {
            currentTimerJob?.cancel()
        }
    }

    fun resume() {
        check(isRunning) { ERROR_TIMER_NOT_BEEN_STARTED }
        check(isPausing) { ERROR_TIMER_NOT_BEEN_PAUSED }
        isPausing = false
        start()
    }
}
