package com.audiolearning.app.timer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
class TimerTest {

    private lateinit var timer: Timer

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        // Setup KotlinCoroutines
        Dispatchers.setMain(newSingleThreadContext("TestingThread"))

        timer = Timer()
    }

    @After
    fun stopTimer() {
        try {
            timer.stop()
        } catch (illegalStateException: IllegalStateException) {
            // Do nothing, since this should only happen when testing
        }
    }

    /* start() */
    @Test(expected = IllegalStateException::class)
    fun start_ShouldNotBeCallableTwice() {
        timer.start()
        timer.start()
    }

    @Test
    fun start_timeShouldBeXSecondsAfterXSeconds() {
        val testTime = 2100L
        val expectedTimeString = TimeMutableLiveData()
            .apply {
                setValueFromMillis(testTime)
            }.value

        timer.start()
        runBlocking {
            delay(testTime)
        }

        assertEquals(expectedTimeString, timer.time.value)
    }

    /* stop() */
    @Test
    fun stop_ShouldStopAndResetTime() {
        val expectedTimeString = TimeMutableLiveData()
            .apply {
                setValueFromMillis(0)
            }.value

        timer.start()
        timer.stop()
        assertEquals(expectedTimeString, timer.time.value)
    }

    /* pause() */
    @Test(expected = IllegalStateException::class)
    fun pause_ShouldNotBeCallableBeforeStart() {
        timer.pause()
    }

    @Test(expected = IllegalStateException::class)
    fun pause_ShouldNotBeCallableTwice() {
        timer.start()
        timer.pause()
        timer.pause()
    }

    @Test
    fun pause_ShouldPauseTime() {
        val testTime = 2100L
        val expectedTimeString = TimeMutableLiveData()
            .apply {
                setValueFromMillis(testTime)
            }.value

        timer.start()
        runBlocking {
            delay(testTime)
        }
        timer.pause()

        assertEquals(expectedTimeString, timer.time.value)
    }

    @Test
    fun pauseThenStop_ShouldResetTime() {
        val expectedTimeString = TimeMutableLiveData()
            .apply {
                setValueFromMillis(0)
            }.value

        timer.start()
        timer.pause()
        timer.stop()

        assertEquals(expectedTimeString, timer.time.value)
    }

    /* resume() */
    @Test(expected = IllegalStateException::class)
    fun resume_ShouldNotBeCallableBeforeStart() {
        timer.resume()
    }

    @Test(expected = IllegalStateException::class)
    fun resume_ShouldNotBeCallableBeforePause() {
        timer.start()
        timer.resume()
    }

    @Test
    fun resume_ShouldResumePausedTime() {
        val testTime = 1100L
        val expectedTimeString = TimeMutableLiveData()
            .apply {
                setValueFromMillis(testTime * 2)
            }.value

        timer.start()
        runBlocking {
            delay(testTime)
        }
        timer.pause()
        timer.resume()
        runBlocking {
            delay(testTime)
        }

        assertEquals(expectedTimeString, timer.time.value)
    }

    @Test
    fun resumeThenStop_ShouldResetTime() {
        val expectedTimeString = TimeMutableLiveData()
            .apply {
                setValueFromMillis(0)
            }.value

        timer.start()
        timer.pause()
        timer.resume()
        timer.stop()

        assertEquals(expectedTimeString, timer.time.value)
    }
}
