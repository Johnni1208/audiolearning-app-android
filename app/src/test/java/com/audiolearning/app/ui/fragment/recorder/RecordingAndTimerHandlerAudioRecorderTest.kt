package com.audiolearning.app.ui.fragment.recorder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.audiolearning.app.audio.recorder.AudioRecorder
import com.audiolearning.app.timer.Timer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

class RecordingAndTimerHandlerAudioRecorderTest {
    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var handler: RecordingAndTimerHandler
    private lateinit var mockAudioRecorder: AudioRecorder
    private lateinit var mockTimer: Timer

    @Before
    fun setUpViewModel() {
        mockAudioRecorder = mock()
        mockTimer = mock()

        handler =
            RecordingAndTimerHandler(
                mockAudioRecorder,
                mockTimer
            )
    }

    @Test
    fun whenFirstTimeCalled_onRecordOrStop_ShouldStartTheRecorder() {
        handler.onRecordOrStop()
        verify(mockAudioRecorder).record()
    }

    @Test
    fun whenSecondTimeCalled_onRecordOrStop_ShouldStopTheRecorder() {
        runBlocking {
            whenever(mockAudioRecorder.stop()).thenReturn(File(String()))

            handler.onRecordOrStop()
            handler.onRecordOrStop()
            delay(100)

            verify(mockAudioRecorder).stop()
        }
    }

    @Test
    fun whenSecondTimeCalled_onRecordOrStop_recordedFileShouldNotBeNull() {
        runBlocking {
            whenever(mockAudioRecorder.stop()).thenReturn(File(String()))

            handler.onRecordOrStop()
            handler.onRecordOrStop()
            delay(100)

            assertTrue(handler.recordedFile.value != null)
        }
    }

    @Test
    fun whenFirstTimeCalled_onPauseOrResume_ShouldPauseTheRecorder() {
        handler.onPauseOrResume()
        verify(mockAudioRecorder).pause()
    }

    @Test
    fun whenSecondTimeCalled_onPauseOrResume_ShouldResumeTheRecorder() {
        handler.onPauseOrResume()
        handler.onPauseOrResume()
        verify(mockAudioRecorder).resume()
    }

    @Test
    fun whenFirstOnPauseOrResume_thenOnRecordOrStop_ShouldStopTheRecorder() {
        runBlocking {
            handler.onPauseOrResume()
            handler.onRecordOrStop()
            delay(100)

            verify(mockAudioRecorder).stop()
        }
    }

    @Test
    fun whenRecorderIsActive_onDestroy_ShouldCallOnDestroyOfRecorder() {
        whenever(mockAudioRecorder.isActive).thenReturn(true)
        handler.onDestroy()

        verify(mockAudioRecorder).onDestroy()
    }

    @Test
    fun whenRecorderIsNotActive_onDestroy_ShouldCallNothing() {
        whenever(mockAudioRecorder.isActive).thenReturn(false)
        handler.onDestroy()

        verify(mockAudioRecorder, never()).onDestroy()
    }
}