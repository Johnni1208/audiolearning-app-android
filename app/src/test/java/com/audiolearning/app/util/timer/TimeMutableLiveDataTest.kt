package com.audiolearning.app.util.timer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.audiolearning.app.timer.TimeMutableLiveData
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class TimeMutableLiveDataTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun class_HasRightStringInitially() {
        assertEquals("00:00", TimeMutableLiveData().value)
    }

    @Test(expected = NoSuchMethodException::class)
    fun setValue_ShouldNotBeCallable() {
        TimeMutableLiveData().value = "xyz"
    }

    @Test
    fun setValueFromMillis_ReturnsTheRightString() {
        val expectedString = "00:01"
        val outputTimeString = TimeMutableLiveData().apply {
            setValueFromMillis(1000L)
        }.value

        assertEquals(expectedString, outputTimeString)
    }
}
