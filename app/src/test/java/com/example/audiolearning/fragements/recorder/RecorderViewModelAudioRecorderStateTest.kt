package com.example.audiolearning.fragements.recorder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.audiolearning.audio.audio_recorder.AudioRecorderState
import com.example.audiolearning.fragments.recorder.RecorderViewModel
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RecorderViewModelAudioRecorderStateTest {
    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RecorderViewModel

    @Before
    fun setUp() {
        viewModel = RecorderViewModel(mock())
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
}