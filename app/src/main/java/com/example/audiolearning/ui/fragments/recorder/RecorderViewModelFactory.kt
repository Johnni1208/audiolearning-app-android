package com.example.audiolearning.ui.fragments.recorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.audiolearning.audio.audio_recorder.IAudioRecorder
import com.example.audiolearning.util.timer.ITimer

class RecorderViewModelFactory(
    private val audioRecorder: IAudioRecorder,
    private val timer: ITimer
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecorderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecorderViewModel(audioRecorder, timer) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}