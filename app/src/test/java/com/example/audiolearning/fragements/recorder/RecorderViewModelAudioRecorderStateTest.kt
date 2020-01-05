package com.example.audiolearning.fragements.recorder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.audiolearning.audio.audio_recorder.AudioRecorderState
import com.example.audiolearning.audio.audio_recorder.IAudioRecorder
import com.example.audiolearning.audio.audio_store.IAudioStore
import com.example.audiolearning.components.fragments.recorder.RecorderViewModel
import com.example.audiolearning.util.timer.ITimer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RecorderViewModelAudioRecorderStateTest {
    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RecorderViewModel
    private lateinit var mockAudioRecorder: IAudioRecorder
    private lateinit var mockTimer: ITimer
    private lateinit var mockAudioStore: IAudioStore


    @Before
    fun setUpViewModel() {
        mockAudioRecorder = mock()
        mockTimer = mock()
        mockAudioStore = mock()


        viewModel =
            RecorderViewModel(
                mockAudioRecorder,
                mockTimer,
                mockAudioStore
            )
    }

    @Test
    fun atBeginning_StateShouldBeIDLING() {
        assertEquals(viewModel.audioRecorderState.value, AudioRecorderState.IDLING)
    }

    @Test
    fun whenOddTimesCalled_onRecordOrStop_StateShouldBeRECORDING() {
        viewModel.onRecordOrStop()

        assertEquals(viewModel.audioRecorderState.value, AudioRecorderState.RECORDING)
    }

    @Test
    fun whenEvenTimesCalled_onRecordOrStop_StateShouldBeIDLING() {
        viewModel.onRecordOrStop()
        viewModel.onRecordOrStop()

        assertEquals(viewModel.audioRecorderState.value, AudioRecorderState.IDLING)
    }

    @Test
    fun whenOddTimesCalled_onPauseOrResume_StateShouldBePAUSING() {
        viewModel.onPauseOrResume()

        assertEquals(viewModel.audioRecorderState.value, AudioRecorderState.PAUSING)
    }

    @Test
    fun whenEvenTimesCalled_onPauseOrResume_StateShouldBeRECORDING() {
        viewModel.onPauseOrResume()
        viewModel.onPauseOrResume()

        assertEquals(viewModel.audioRecorderState.value, AudioRecorderState.RECORDING)
    }

    @Test
    fun onDestroy_StateShouldBeIDLING() {
        whenever(mockAudioRecorder.isActive).thenReturn(true)
        viewModel.onDestroy()

        assertEquals(viewModel.audioRecorderState.value, AudioRecorderState.IDLING)
    }
}