package com.example.audiolearning.audio.audio_recorder

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File

/**
 * This class provides a custom made MediaRecorder, specialised for audio recordings.
 */
interface IAudioRecorder {

    /**
     * Flag for determining if the recorder is currently active.
     */
    var isActive: Boolean

    fun record()

    @RequiresApi(Build.VERSION_CODES.N)
    fun pause()

    @RequiresApi(Build.VERSION_CODES.N)
    fun resume()

    /**
     *
     * Stops the recording and returns the temporary file.
     *
     * IMPORTANT: This function needs 500 milliseconds delay,
     * since the MPEG_4 audio format cuts audio to early.
     *
     */
    suspend fun stop(): File

    /**
     * Use this function when destroying the fragment.
     * It cancels the recorder without providing the file.
     *
     * We cannot use [stop] for this, since it takes 500ms before finally stopping.
     */
    fun onDestroy()
}