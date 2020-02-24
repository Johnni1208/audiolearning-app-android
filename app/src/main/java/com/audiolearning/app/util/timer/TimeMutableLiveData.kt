package com.audiolearning.app.util.timer

import androidx.lifecycle.MutableLiveData

/**
 * This class is an extension of [MutableLiveData].
 * Only usable with the [setValueFromMillis].
 */
class TimeMutableLiveData : MutableLiveData<String>("00:00:00") {

    /**
     * Sets the value of this MutableLiveData to a formatted string ("00:00:00"),
     * accordingly to the given millis.
     *
     * @param millis Milliseconds to be processed
     */
    fun setValueFromMillis(millis: Long) {
        val hours = millis / 1000 / 60 / 60
        val minutes = millis / 1000 / 60 % 60
        val seconds = millis / 1000 % 60

        val timeString = "${if (hours > 9) "$hours" else "0$hours"}:" +
                "${if (minutes > 9) "$minutes" else "0$minutes"}:" +
                if (seconds > 9) "$seconds" else "0$seconds"

        super.setValue(timeString)
    }

    /**
     * Use [setValueFromMillis] instead, since this data type should only
     * process milliseconds into text.
     */
    override fun setValue(value: String?) {
        throw NoSuchMethodException()
    }
}