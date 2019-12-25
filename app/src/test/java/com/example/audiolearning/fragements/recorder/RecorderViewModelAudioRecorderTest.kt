package com.example.audiolearning.fragements.recorder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.audiolearning.audio.audio_recorder.IAudioRecorder
import com.example.audiolearning.fragments.recorder.RecorderViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

class RecorderViewModelAudioRecorderTest {
    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RecorderViewModel
    private lateinit var mockAudioRecorder: IAudioRecorder

    @Before
    fun setUp() {
        mockAudioRecorder = mock()
        viewModel = RecorderViewModel(mockAudioRecorder)
    }

    @Test
    fun whenFirstTimeCalled_onRecordOrStop_ShouldStartTheRecorder() {
        viewModel.onRecordOrStop()
        verify(mockAudioRecorder).record()
    }

    @Test
    fun whenSecondTimeCalled_onRecordOrStop_ShouldStopTheRecorder() {
        runBlocking {
            whenever(mockAudioRecorder.stop()).thenReturn(File(String()))

            viewModel.onRecordOrStop()
            viewModel.onRecordOrStop()

            verify(mockAudioRecorder).stop()
        }
    }

    @Test
    fun whenSecondTimeCalled_onRecordOrStop_recordedFileShouldNotBeNull() {
        runBlocking {
            whenever(mockAudioRecorder.stop()).thenReturn(File(String()))

            viewModel.onRecordOrStop()
            viewModel.onRecordOrStop()

            assertTrue(viewModel.recordedFile.value != null)
        }
    }

    @Test
    fun whenFirstTimeCalled_onPauseOrResume_ShouldPauseTheRecorder() {
        viewModel.onPauseOrResume()
        verify(mockAudioRecorder).pause()
    }

    @Test
    fun whenSecondTimeCalled_onPauseOrResume_ShouldResumeTheRecorder() {
        viewModel.onPauseOrResume()
        viewModel.onPauseOrResume()
        verify(mockAudioRecorder).resume()
    }

    @Test
    fun whenRecorderIsActive_onDestroy_ShouldCallOnDestroyOfRecorder() {
        whenever(mockAudioRecorder.isActive).thenReturn(true)
        viewModel.onDestroy()

        verify(mockAudioRecorder).onDestroy()
    }

    @Test
    fun whenRecorderIsNotActive_onDestroy_ShouldCallNothing() {
        whenever(mockAudioRecorder.isActive).thenReturn(false)
        viewModel.onDestroy()

        verify(mockAudioRecorder, never()).onDestroy()
    }
}