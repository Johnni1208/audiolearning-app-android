package com.example.audiolearning.audio.audio_recorder

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File

/**
 * This class must provide a custom made MediaRecorder, specialised for audio recordings.
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

    suspend fun stop(): File

    fun onDestroy()
}