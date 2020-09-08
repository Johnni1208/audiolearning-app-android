package com.audiolearning.app.audio.recorder

import android.media.MediaRecorder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AudioRecorderTest {

    private lateinit var recorder: AudioRecorder
    private lateinit var mockMediaRecorder: MediaRecorder

    @Before
    fun setupAudioRecorder() {
        mockMediaRecorder = mock()
        recorder = AudioRecorder(mockMediaRecorder)
    }

    /* record() */
    @Test
    fun record_ShouldSetStateToRECORDING() {
        recorder.record()

        assertEquals(AudioRecorderState.RECORDING, recorder.state)
    }

    @Test
    fun record_ShouldPrepareAndStartTheMediaRecorder() {
        recorder.record()

        verify(mockMediaRecorder).prepare()
        verify(mockMediaRecorder).start()
    }

    /* stop() */
    @Test
    fun stop_ShouldSetStateToIDLING() = runBlockingTest {
        recorder.stop()

        assertEquals(AudioRecorderState.IDLING, recorder.state)
    }

    @Test
    fun stop_ShouldStopAndReleaseTheMediaRecorder() = runBlockingTest {
        recorder.stop()

        verify(mockMediaRecorder).stop()
        verify(mockMediaRecorder).release()
    }

    @Test
    fun stop_ShouldReturnRecordedFile() = runBlockingTest {
        val recordedFile = recorder.stop()

        assertTrue(recordedFile.name.contains("tempAudioFile"))
        assertTrue(recordedFile.name.contains(".m4a"))
    }

    /* pause() */
    @Test
    fun pause_ShouldPauseTheMediaRecorder() {
        recorder.pause()

        verify(mockMediaRecorder).pause()
    }

    @Test
    fun pause_ShouldSetStateToPAUSING() {
        recorder.pause()

        assertEquals(AudioRecorderState.PAUSING, recorder.state)
    }

    /* resume() */
    @Test
    fun resume_ShouldResumeTheMediaRecorder() {
        recorder.resume()

        verify(mockMediaRecorder).resume()
    }

    /* onDestroy() */
    @Test
    fun onDestroy_ShouldSetStateToIDLING() {
        recorder.onDestroy()

        assertEquals(AudioRecorderState.IDLING, recorder.state)
    }

    @Test
    fun onDestroy_ShouldStopAndReleaseTheMediaRecorder() {
        recorder.onDestroy()

        verify(mockMediaRecorder).stop()
        verify(mockMediaRecorder).release()
    }
}
