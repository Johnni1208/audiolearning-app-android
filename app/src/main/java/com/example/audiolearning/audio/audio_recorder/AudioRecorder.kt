package com.example.audiolearning.audio.audio_recorder

import android.annotation.TargetApi
import android.media.MediaRecorder
import android.os.Build
import kotlinx.coroutines.delay
import java.io.File

class AudioRecorder : IAudioRecorder {
    private val tempAudioFile: File = File.createTempFile("tempAudioFile", ".m4a")
    private lateinit var recorder: MediaRecorder

    override fun record() {
        recorder = AudioRecorderProvider.getAudioRecorder(tempAudioFile)

        recorder.apply {
            prepare()
            start()
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    override fun pause() {
        recorder.pause()
    }

    @TargetApi(Build.VERSION_CODES.N)
    override fun resume() {
        recorder.resume()
    }

    /*
     * The stop() function needs 500 milliseconds delay,
     * since the MPEG_4 audio format cuts audio to early.
     */
    override suspend fun stop(): File {
        delay(500)
        recorder.apply {
            stop()
            release()
        }
        return tempAudioFile
    }
}