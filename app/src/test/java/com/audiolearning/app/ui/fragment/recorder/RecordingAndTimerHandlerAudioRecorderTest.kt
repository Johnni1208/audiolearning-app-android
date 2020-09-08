package com.audiolearning.app.ui.fragment.recorder

import android.media.MediaRecorder
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.audiolearning.app.audio.recorder.AudioRecorder
import com.audiolearning.app.audio.recorder.AudioRecorderState
import com.audiolearning.app.timer.Timer
import com.audiolearning.app.ui.fragment.pager.recorder.RecordingAndTimerHandler
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RecordingAndTimerHandlerAudioRecorderTest {
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
    fun whenFirstTimeCalled_onRecordOrStop_ShouldStartTheRecorder() {
        handler.onRecordOrStop()
        assertEquals(AudioRecorderState.RECORDING, recorder.state)
    }

    @Test
    fun whenSecondTimeCalled_onRecordOrStop_ShouldStopTheRecorder() {
        runBlocking {
            handler.onRecordOrStop()
            handler.onRecordOrStop()
            delay(550)

            assertEquals(AudioRecorderState.IDLING, recorder.state)
        }
    }

    @Test
    fun whenSecondTimeCalled_onRecordOrStop_recordedFileShouldNotBeNull() {
        runBlocking {
            handler.onRecordOrStop()
            handler.onRecordOrStop()
            delay(550)

            assertTrue(handler.recordedFile.value != null)
        }
    }

    @Test
    fun whenFirstTimeCalled_onPauseOrResume_ShouldPauseTheRecorder() {
        handler.onPauseOrResume()
        assertEquals(AudioRecorderState.PAUSING, recorder.state)
    }

    @Test
    fun whenSecondTimeCalled_onPauseOrResume_ShouldResumeTheRecorder() {
        handler.onPauseOrResume()
        handler.onPauseOrResume()

        assertEquals(AudioRecorderState.RECORDING, recorder.state)
    }

    @Test
    fun whenFirstOnPauseOrResume_thenOnRecordOrStop_ShouldStopTheRecorder() {
        runBlocking {
            handler.onPauseOrResume()
            handler.onRecordOrStop()
            delay(550)

            assertEquals(AudioRecorderState.IDLING, recorder.state)
        }
    }

    @Test
    fun whenRecorderRECORDING_onDestroy_ShouldCallOnDestroyOfRecorder() {
        recorder.record()
        handler.onDestroy()

        assertEquals(AudioRecorderState.IDLING, recorder.state)
    }
}
