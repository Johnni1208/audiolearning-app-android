package com.audiolearning.app.audio.recorder

import android.annotation.TargetApi
import android.media.MediaRecorder
import android.os.Build
import com.audiolearning.app.data.db.entities.Audio
import kotlinx.coroutines.delay
import java.io.File
import javax.inject.Inject

/**
 * AudioRecorder for recording audio.
 * Use this for recording audio in this project.
 *
 * @param recorder **Used for testing purposes only. Use _null_ in production.**
 *
 * Inject a custom instance of [MediaRecorder],
 * else it creates an instance in the [record] method.
 */
class AudioRecorder @Inject constructor(private var recorder: MediaRecorder?) {
    var state: AudioRecorderState = AudioRecorderState.IDLING
    val maxAmplitude: Int
        get() = recorder!!.maxAmplitude

    private val audioSamplingRate = 16000
    private val audioEncodingBitRate = 64000
    private val timeBeforeStop = 500L

    private val tempAudioFile: File =
        File.createTempFile("tempAudioFile", Audio.fileExtension)

    fun record() {
        if (recorder == null) {
            recorder = getAudioMediaRecorder(tempAudioFile)
        }

        recorder?.apply {
            prepare()
            start()
        }

        state = AudioRecorderState.RECORDING
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun pause() {
        recorder?.pause()
        state = AudioRecorderState.PAUSING
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun resume() {
        recorder?.resume()
        state = AudioRecorderState.RECORDING
    }

    /**
     * Stops the recording and returns the temporary file.
     *
     * __IMPORTANT__: This function needs 500 milliseconds delay,
     * since the MPEG_4 audio format cuts audio to early.
     *
     */
    suspend fun stop(): File {
        delay(timeBeforeStop)
        recorder?.apply {
            stop()
            reset()
            release()
        }

        recorder = null
        state = AudioRecorderState.IDLING
        return tempAudioFile
    }

    /**
     * Use this function when destroying the fragment.
     * It cancels the recorder without providing the file.
     *
     * We cannot use [stop] for this, since it takes 500ms before finally stopping.
     */
    fun onDestroy() {
        recorder?.apply {
            stop()
            release()
        }

        recorder = null
        state = AudioRecorderState.IDLING
    }

    private fun getAudioMediaRecorder(file: File) = MediaRecorder().apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        setOutputFile(file.absolutePath)
        setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        setAudioEncodingBitRate(audioEncodingBitRate)
        setAudioSamplingRate(audioSamplingRate)
    }
}
