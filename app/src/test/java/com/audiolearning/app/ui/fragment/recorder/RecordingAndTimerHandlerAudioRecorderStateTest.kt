package com.audiolearning.app.ui.fragment.recorder

import android.media.MediaRecorder
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.audiolearning.app.audio.recorder.AudioRecorder
import com.audiolearning.app.audio.recorder.AudioRecorderState
import com.audiolearning.app.timer.Timer
import com.audiolearning.app.ui.fragment.pager.recorder.RecordingAndTimerHandler
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RecordingAndTimerHandlerAudioRecorderStateTest {
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
    fun atBeginning_StateShouldBeIDLING() {
        assertEquals(handler.audioRecorderState.value, AudioRecorderState.IDLING)
    }

    @Test
    fun whenOddTimesCalled_onRecordOrStop_StateShouldBeRECORDING() {
        handler.onRecordOrStop()

        assertEquals(handler.audioRecorderState.value, AudioRecorderState.RECORDING)
    }

    @Test
    fun whenEvenTimesCalled_onRecordOrStop_StateShouldBeIDLING() {
        handler.onRecordOrStop()
        handler.onRecordOrStop()

        assertEquals(handler.audioRecorderState.value, AudioRecorderState.IDLING)
    }

    @Test
    fun whenOddTimesCalled_onPauseOrResume_StateShouldBePAUSING() {
        handler.onPauseOrResume()

        assertEquals(handler.audioRecorderState.value, AudioRecorderState.PAUSING)
    }

    @Test
    fun whenEvenTimesCalled_onPauseOrResume_StateShouldBeRECORDING() {
        handler.onPauseOrResume()
        handler.onPauseOrResume()

        assertEquals(handler.audioRecorderState.value, AudioRecorderState.RECORDING)
    }

    @Test
    fun onDestroy_StateShouldBeIDLING() {
        recorder.record()
        handler.onDestroy()

        assertEquals(handler.audioRecorderState.value, AudioRecorderState.IDLING)
    }
}
