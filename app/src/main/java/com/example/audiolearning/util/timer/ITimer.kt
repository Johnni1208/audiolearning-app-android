package com.example.audiolearning.util.timer

import androidx.lifecycle.LiveData

/**
 * This class provides a Timer which counts up.
 * Only usable with Android LiveData.
 */
interface ITimer {
    val time: LiveData<String>

    fun start()
    fun stop()
    fun pause()
    fun resume()
}