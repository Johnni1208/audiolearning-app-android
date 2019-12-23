package com.example.audiolearning.audio.audio_recorder

import android.media.MediaRecorder
import java.io.File

class AudioRecorderProvider {
    companion object {
        fun getAudioRecorder(file: File): MediaRecorder {
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
}