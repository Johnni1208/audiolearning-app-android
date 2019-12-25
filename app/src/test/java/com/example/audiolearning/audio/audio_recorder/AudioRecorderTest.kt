package com.example.audiolearning.audio.audio_recorder

import android.media.MediaRecorder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

class AudioRecorderTest {

    private lateinit var recorder: AudioRecorder
    private lateinit var mockMediaRecorder: MediaRecorder

    @Before
    fun setupAudioRecorder() {
        mockMediaRecorder = mock()
        recorder = AudioRecorder(mockMediaRecorder)
    }

    @Test
    fun record_ShouldSetIsActiveToTrue() {
        recorder.record()

        assertEquals(true, recorder.isActive)
    }

    @Test
    fun record_ShouldPrepareAndStartTheMediaRecorder() {
        recorder.record()

        verify(mockMediaRecorder).prepare()
        verify(mockMediaRecorder).start()
    }

    @Test
    fun stop_ShouldSetIsActiveToFalse() {
        runBlocking {
            recorder.stop()
        }

        assertEquals(false, recorder.isActive)
    }

    @Test
    fun stop_ShouldStopAndReleaseTheMediaRecorder() {
        runBlocking {
            recorder.stop()
        }

        verify(mockMediaRecorder).stop()
        verify(mockMediaRecorder).release()
    }

    @Test
    fun stop_ShouldNeed500msTime() {
        val startTime: Long = System.currentTimeMillis()

        runBlocking {
            recorder.stop()
        }

        val endTime: Long = System.currentTimeMillis()

        // Only check if around 500 - 600ms (execution time must also be considered)
        assertEquals('5', (endTime - startTime).toString()[0])
    }

    @Test
    fun stop_ShouldReturnRecordedFile() {
        var recordedFile: File? = null

        runBlocking {
            recordedFile = recorder.stop()
        }

        assertTrue(recordedFile!!.name.contains("tempAudioFile"))
        assertTrue(recordedFile!!.name.contains(".m4a"))
    }

    @Test
    fun pause_ShouldPauseTheMediaRecorder() {
        recorder.pause()

        verify(mockMediaRecorder).pause()
    }

    @Test
    fun resume_ShouldResumeTheMediaRecorder() {
        recorder.resume()

        verify(mockMediaRecorder).resume()
    }

    @Test
    fun onDestroy_ShouldSetIsActiveToFalse() {
        recorder.onDestroy()

        assertEquals(false, recorder.isActive)
    }

    @Test
    fun onDestroy_ShouldStopAndReleaseTheMediaRecorder() {
        recorder.onDestroy()

        verify(mockMediaRecorder).stop()
        verify(mockMediaRecorder).release()
    }
}