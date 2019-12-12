package com.example.audiolearning.util.timer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mock
import kotlinx.coroutines.*
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class TimerUnitTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var timer: Timer
    private val observer: Observer<String> = mock()
    private val captor: ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)

    @Before
    fun setUp() {
        // Setup KotlinCoroutines
        Dispatchers.setMain(newSingleThreadContext("TestingThread"))

        // Setup Timer mock
        timer = Timer()
        timer.time.observeForever(observer)
    }

    @After
    fun stopTimer(){
        try{
            timer.stop()
        }catch (illegalStateException: IllegalStateException){
            // Do nothing, since this should only happen when testing
        }
    }

    @Test(expected = IllegalStateException::class)
    fun start_ShouldNotBeCallableTwice(){
        timer.start()
        timer.start()
    }

    @Test
    fun start_timeShouldBeXSecondsAfterXSeconds() {
        val testTime = 2100L
        val expectedTimeString = TimeMutableLiveData().apply {
            setValueFromMillis(testTime)
        }.value

        timer.start()
        runBlocking {
            delay(testTime)
        }

        captor.run {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(expectedTimeString, value)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun stop_ShouldNotBeCallableBeforeStart() {
        timer.stop()
    }

    @Test
    fun stop_ShouldStopAndResetTime() {
        val expectedTimeString = TimeMutableLiveData().apply {
            setValueFromMillis(0)
        }.value

        timer.start()
        timer.stop()
        captor.run {
            verify(observer, times(2)).onChanged(capture())
            assertEquals(expectedTimeString, value)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun pause_ShouldNotBeCallableBeforeStart(){
        timer.pause()
    }

    @Test
    fun pause_ShouldPauseTime(){
        val testTime = 2100L
        val expectedTimeString = TimeMutableLiveData().apply {
            setValueFromMillis(testTime)
        }.value

        timer.start()
        runBlocking {
            delay(testTime)
        }
        timer.pause()

        captor.run {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(expectedTimeString, value)
        }
    }

    @Test
    fun resume_ShouldResumePausedTime(){
        val testTime = 1100L
        val expectedTimeString = TimeMutableLiveData().apply {
            setValueFromMillis(testTime*2)
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

        captor.run {
            verify(observer, times(3)).onChanged(capture())
            assertEquals(expectedTimeString, value)
        }
    }
}