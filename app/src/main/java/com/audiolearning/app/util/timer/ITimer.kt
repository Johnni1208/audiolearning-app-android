package com.audiolearning.app.util.timer

import androidx.lifecycle.LiveData

interface ITimer {
    val time: LiveData<String>

    fun start()
    fun stop()
    fun pause()
    fun resume()
}