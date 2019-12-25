package com.example.audiolearning.audio.audio_recorder

import android.annotation.TargetApi
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import kotlinx.coroutines.delay
import java.io.File

/**
 * AudioRecorder for recording audio.
 * Use this for recording audio in this project.
 *
 * @param recorder Inject a custom instance of [MediaRecorder],
 * else it creates an instance in the [record] method.
 * Used for testing purposes.
 */
class AudioRecorder(private var recorder: MediaRecorder? = null) : IAudioRecorder {

    override var isActive: Boolean = false

    private val tempDir: File =
        File(Environment.getExternalStorageDirectory(), "/AudioLearning/temp")
    private val tempAudioFile: File

    init {
        if (!tempDir.exists()) tempDir.mkdirs()

        tempAudioFile = File.createTempFile("tempAudioFile", ".m4a", tempDir)
    }

    override fun record() {
        if (recorder == null) recorder =
            CustomMediaRecorderProvider.getNewAudioRecorder(tempAudioFile)

        recorder!!.apply {
            prepare()
            start()
        }
        isActive = true
    }

    @TargetApi(Build.VERSION_CODES.N)
    override fun pause() {
        recorder!!.pause()
    }

    @TargetApi(Build.VERSION_CODES.N)
    override fun resume() {
        recorder!!.resume()
    }

    override suspend fun stop(): File {
        delay(500)
        recorder!!.apply {
            stop()
            release()
        }

        recorder = null
        isActive = false
        return tempAudioFile
    }

    override fun onDestroy() {
        recorder!!.apply {
            stop()
            release()
        }

        recorder = null
        isActive = false
    }
}