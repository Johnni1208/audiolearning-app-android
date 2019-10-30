package com.example.audiolearning.audio_recorder

import android.media.MediaRecorder

class AudioRecorder : MediaRecorder() {
    init{
        setAudioSource(AudioSource.MIC)
        setOutputFormat(OutputFormat.MPEG_4)
        setAudioEncoder(AudioEncoder.AMR_NB)
        setAudioEncodingBitRate(16)
        setAudioSamplingRate(44100)
    }
}