package com.audiolearning.app.util.audio

import android.media.MediaRecorder
import java.io.File

class CustomMediaRecorderProvider {
    companion object {
        fun getAudioMediaRecorder(file: File) = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(64000)
            setAudioSamplingRate(16000)
            setOutputFile(file.absolutePath)
        }
    }
}