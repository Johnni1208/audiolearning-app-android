package com.audiolearning.app.util.timer

import androidx.lifecycle.MutableLiveData
import com.audiolearning.app.extension.toTimeString

/**
 * This class is an extension of [MutableLiveData].
 * Only usable with the [setValueFromMillis].
 */
class TimeMutableLiveData : MutableLiveData<String>("00:00") {

    /**
     * Sets the value of this MutableLiveData to a formatted string ("00:00:00"),
     * accordingly to the given millis.
     *
     * @param millis Milliseconds to be processed
     */
    fun setValueFromMillis(millis: Long) {
        super.setValue(millis.toTimeString())
    }

    /**
     * Use [setValueFromMillis] instead, since this data type should only
     * process milliseconds into text.
     */
    override fun setValue(value: String?) {
        throw NoSuchMethodException()
    }
}
