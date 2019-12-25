package com.example.audiolearning.audio.audio_recorder

import android.annotation.TargetApi
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import kotlinx.coroutines.delay
import java.io.File

class AudioRecorder : IAudioRecorder {

    override var isActive: Boolean = false

    private val tempDir: File =
        File(Environment.getExternalStorageDirectory(), "/AudioLearning/temp")
    private val tempAudioFile: File
    private lateinit var recorder: MediaRecorder

    init {
        if (!tempDir.exists()) tempDir.mkdirs()

        tempAudioFile = File.createTempFile("tempAudioFile", ".m4a", tempDir)
    }

    override fun record() {
        recorder = getNewAudioRecorder(tempAudioFile)

        recorder.apply {
            prepare()
            start()
        }
        isActive = true
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
        isActive = false
        return tempAudioFile
    }

    override fun onDestroy() {
        recorder.apply {
            stop()
            release()
        }
        isActive = false
    }

    private fun getNewAudioRecorder(file: File): MediaRecorder {
        return MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(64000)
            setAudioSamplingRate(16000)
            setOutputFile(file.absolutePath)
        }
    }
}