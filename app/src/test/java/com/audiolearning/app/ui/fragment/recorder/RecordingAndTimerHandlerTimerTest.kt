package com.audiolearning.app.ui.fragment.recorder

import android.media.MediaRecorder
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.audiolearning.app.audio.recorder.AudioRecorder
import com.audiolearning.app.timer.Timer
import com.audiolearning.app.ui.fragment.pager.recorder.RecordingAndTimerHandler
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RecordingAndTimerHandlerTimerTest {
    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var handler: RecordingAndTimerHandler
    private lateinit var recorder: AudioRecorder
    private lateinit var mockTimer: Timer

    @Before
    fun setUpViewModel() {
        val mockMediaRecorder: MediaRecorder = mock()

        recorder = AudioRecorder(mockMediaRecorder)
        mockTimer = mock()

        handler =
            RecordingAndTimerHandler(
                recorder,
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
    fun whenRecorderIsRECORDING_onDestroy_ShouldCallStopOfTimer() {
        recorder.record()
        handler.onDestroy()

        verify(mockTimer).stop()
    }

    @Test
    fun whenRecorderIsIDLING_onDestroy_ShouldCallNothing() {
        handler.onDestroy()

        verify(mockTimer, never()).stop()
    }
}
