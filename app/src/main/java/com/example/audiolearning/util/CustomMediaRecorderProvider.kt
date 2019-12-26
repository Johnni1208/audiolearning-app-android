package com.example.audiolearning.util

import android.media.MediaRecorder
import com.example.audiolearning.util.CustomMediaRecorderProvider.Companion.getRecorderForAudio
import java.io.File

/**
 * Provides a custom MediaRecorder of any kind.
 *
 * Currently only use [getRecorderForAudio].
 */
class CustomMediaRecorderProvider {
    companion object {
        /**
         * Returns a ready-to-use AudioRecorder.
         *
         * @param file The file where the output should be written.
         */
        fun getRecorderForAudio(file: File): MediaRecorder {
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