package com.audiolearning.app.util.audio

import android.media.MediaRecorder

class CustomMediaRecorderProvider {
    companion object {
        fun getAudioMediaRecorder() = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(64000)
            setAudioSamplingRate(16000)
        }
    }
}