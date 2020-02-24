package com.audiolearning.app.ui.fragments.recorder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.audiolearning.app.audio.audio_recorder.IAudioRecorder
import com.audiolearning.app.util.timer.ITimer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RecordingAndTimerHandlerTimerTest {
    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var handler: RecordingAndTimerHandler
    private lateinit var mockAudioRecorder: IAudioRecorder
    private lateinit var mockTimer: ITimer

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
    fun whenFirstTimeCalled_onRecordOrStop_ShouldStartTheTimer() {
        handler.onRecordOrStop()
        verify(mockTimer).start()
    }

    @Test
    fun whenSecondTimeCalled_onRecordOrStop_ShouldStopTheTimer() {
        runBlocking {
            handler.onRecordOrStop()
            handler.onRecordOrStop()

            verify(mockTimer).stop()
        }
    }

    @Test
    fun whenFirstTimeCalled_onPauseOrResume_ShouldPauseTheTimer() {
        handler.onPauseOrResume()
        verify(mockTimer).pause()
    }

    @Test
    fun whenSecondTimeCalled_onPauseOrResume_ShouldResumeTheRecorder() {
        handler.onPauseOrResume()
        handler.onPauseOrResume()
        verify(mockTimer).resume()
    }

    @Test
    fun whenFirstOnPauseOrResume_thenOnRecordOrStop_ShouldStopTheTimer() {
        runBlocking {
            handler.onPauseOrResume()
            handler.onRecordOrStop()

            verify(mockTimer).stop()
        }
    }

    @Test
    fun whenRecorderIsActive_onDestroy_ShouldCallStopOfTimer() {
        whenever(mockAudioRecorder.isActive).thenReturn(true)
        handler.onDestroy()

        verify(mockTimer).stop()
    }

    @Test
    fun whenRecorderIsNotActive_onDestroy_ShouldCallNothing() {
        whenever(mockAudioRecorder.isActive).thenReturn(false)
        handler.onDestroy()

        verify(mockTimer, never()).stop()
    }
}