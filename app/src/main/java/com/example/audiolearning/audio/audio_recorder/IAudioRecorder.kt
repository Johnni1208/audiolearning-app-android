package com.example.audiolearning.audio.audio_recorder

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.File

interface IAudioRecorder {
    var isActive: Boolean

    fun record()

    @RequiresApi(Build.VERSION_CODES.N)
    fun pause()

    @RequiresApi(Build.VERSION_CODES.N)
    fun resume()

    suspend fun stop(): File

    fun onDestroy()
}