package com.example.audiolearning.util.timer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.util.regex.Pattern

class TimeMutableLiveDataTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun class_HasRightStringInitially() {
        assertEquals(TimeMutableLiveData().value, "00:00:00")
    }

    @Test(expected = NoSuchMethodException::class)
    fun setValue_ShouldNotBeCallable() {
        TimeMutableLiveData().value = "xyz"
    }

    @Test
    fun setValueFromMillis_ReturnsRightFormattedTimeString() {
        // 00:00:00
        val expectedFormat = "\\d\\d:\\d\\d:\\d\\d"
        val outputTimeString = TimeMutableLiveData().apply {
            setValueFromMillis(1000L)
        }.value

        assertTrue(Pattern.matches(expectedFormat, outputTimeString!!))
    }

    @Test
    fun setValueFromMillis_ReturnsTheRightString() {
        val expectedString = "00:00:01"
        val outputTimeString = TimeMutableLiveData().apply {
            setValueFromMillis(1000L)
        }.value

        assertEquals(expectedString, outputTimeString)
    }
}