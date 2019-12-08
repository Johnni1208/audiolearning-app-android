package com.example.audiolearning.audio.audio_recorder

import android.annotation.TargetApi
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.File

class AudioRecorder : IAudioRecorder {
    private val tempDir: File =
        File(Environment.getExternalStorageDirectory(), "/AudioLearning/temp")
    private val tempAudioFile: File
    private lateinit var recorder: MediaRecorder

    init {
        if (!tempDir.exists()) tempDir.mkdirs()

        tempAudioFile = File.createTempFile("tempAudioFile", ".m4a", tempDir)
    }

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

    override suspend fun stop(): File {
        delay(500)
        recorder.apply {
            stop()
            release()
        }
        return tempAudioFile
    }
}