package com.example.audiolearning.util.timer

import androidx.lifecycle.MutableLiveData

class TimeMutableLiveData : MutableLiveData<String>("00:00:00") {
    fun setValueFromMillis(millis: Long) {
        val hours = millis / 1000 / 60 / 60
        val minutes = millis / 1000 / 60 % 60
        val seconds = millis / 1000 % 60

        val timeString = "${if (hours > 9) "$hours" else "0$hours"}:" +
                "${if (minutes > 9) "$minutes" else "0$minutes"}:" +
                if (seconds > 9) "$seconds" else "0$seconds"

        super.setValue(timeString)
    }

    // Use setValueFromMillis
    override fun setValue(value: String?) {
        throw NoSuchMethodException()
    }
}