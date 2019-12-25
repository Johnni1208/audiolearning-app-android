package com.example.audiolearning.audio.audio_recorder

import android.media.MediaRecorder
import com.example.audiolearning.audio.audio_recorder.CustomMediaRecorderProvider.Companion.getNewAudioRecorder
import java.io.File

/**
 * Provides a custom MediaRecorder of any kind.
 *
 * Currently only use [getNewAudioRecorder].
 */
class CustomMediaRecorderProvider {
    companion object {
        /**
         * Returns a ready-to-use AudioRecorder.
         *
         * @param file The file where the output should be written.
         */
        fun getNewAudioRecorder(file: File): MediaRecorder {
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