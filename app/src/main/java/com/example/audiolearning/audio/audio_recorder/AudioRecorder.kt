package com.example.audiolearning.audio.audio_recorder

import android.annotation.TargetApi
import android.media.MediaRecorder
import android.os.Build
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

    private val tempAudioFile: File = File.createTempFile("tempAudioFile", ".m4a")

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

    /*
     * The stop() function needs 500 milliseconds delay,
     * since the MPEG_4 audio format cuts audio to early.
     */
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