package com.example.audiolearning.audio.audio_recorder

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

class AudioRecorderTest {

    private lateinit var recorder: AudioRecorder

    @Before
    fun setupAudioRecorder() {
        recorder = AudioRecorder()
    }

    @Test
    fun record_ShouldSetIsActiveToTrue() {
        recorder.record()

        assertEquals(true, recorder.isActive)
    }

    @Test
    fun stop_ShouldSetIsActiveToFalse() {
        recorder.record()

        runBlocking {
            recorder.stop()
        }

        assertEquals(false, recorder.isActive)
    }

    @Test
    fun stop_ShouldNeed500msTime() {
        val startTime: Long = System.currentTimeMillis()
        recorder.record()

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
        recorder.record()

        runBlocking {
            recordedFile = recorder.stop()
        }

        assertTrue(recordedFile!!.name.contains("tempAudioFile"))
        assertTrue(recordedFile!!.name.contains(".m4a"))
    }

    @Test
    fun onDestroy_ShouldSetIsActiveToFalse() {
        recorder.record()
        recorder.onDestroy()

        assertEquals(false, recorder.isActive)
    }
}