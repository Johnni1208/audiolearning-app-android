package com.example.audiolearning.audio.audio_recorder

import android.annotation.TargetApi
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import java.io.File

class AudioRecorder : IAudioRecorder {
    private lateinit var audioFile: File
    private val tempDir: File = Environment.getExternalStorageDirectory()

    private val recorder = MediaRecorder()
    init {
        recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setAudioEncodingBitRate(16)
            setAudioSamplingRate(44100)
        }
    }

    override fun record() {
        audioFile = File.createTempFile("tempAudioFile", ".mp4", tempDir)

        recorder.apply {
            setOutputFile(audioFile.absolutePath)
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

    override fun stop(): File {
        recorder.apply {
            stop()
            release()
        }

        return audioFile
    }
}