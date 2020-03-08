package com.audiolearning.app.ui.fragments.recorder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.audiolearning.app.audio.audio_recorder.AudioRecorder
import com.audiolearning.app.audio.audio_recorder.AudioRecorderState
import com.audiolearning.app.util.timer.Timer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RecordingAndTimerHandlerAudioRecorderStateTest {
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
        whenever(mockAudioRecorder.isActive).thenReturn(true)
        handler.onDestroy()

        assertEquals(handler.audioRecorderState.value, AudioRecorderState.IDLING)
    }
}