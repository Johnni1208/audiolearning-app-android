package com.audiolearning.app.audio.audio_recorder

import android.annotation.TargetApi
import android.media.MediaRecorder
import android.os.Build
import com.audiolearning.app.data.db.entities.Audio
import kotlinx.coroutines.delay
import java.io.File

/**
 * AudioRecorder for recording audio.
 * Use this for recording audio in this project.
 *
 * @param recorder Inject a custom instance of [MediaRecorder],
 * else it creates an instance in the [record] method.
 * Used for testing purposes.
 */
class AudioRecorder(private var recorder: MediaRecorder? = null) : IAudioRecorder {
    override var isActive: Boolean = false

    private val tempAudioFile: File =
        File.createTempFile("tempAudioFile", Audio.fileExtension)

    override fun record() {
        if (recorder == null) {
            recorder = getAudioMediaRecorder(tempAudioFile)
        }

        recorder?.apply {
            prepare()
            start()
        }
        isActive = true
    }

    private fun getAudioMediaRecorder(file: File) = MediaRecorder().apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        setAudioEncodingBitRate(64000)
        setAudioSamplingRate(16000)
        setOutputFile(file.absolutePath)
    }


    @TargetApi(Build.VERSION_CODES.N)
    override fun pause() {
        recorder?.pause()
    }

    @TargetApi(Build.VERSION_CODES.N)
    override fun resume() {
        recorder?.resume()
    }

    /**
     * Stops the recording and returns the temporary file.
     *
     * IMPORTANT: This function needs 500 milliseconds delay,
     * since the MPEG_4 audio format cuts audio to early.
     *
     */
    override suspend fun stop(): File {
        delay(500)
        recorder?.apply {
            stop()
            release()
        }

        recorder = null
        isActive = false
        return tempAudioFile
    }

    /**
     * Use this function when destroying the fragment.
     * It cancels the recorder without providing the file.
     *
     * We cannot use [stop] for this, since it takes 500ms before finally stopping.
     */
    override fun onDestroy() {
        recorder?.apply {
            stop()
            release()
        }

        recorder = null
        isActive = false
    }
}