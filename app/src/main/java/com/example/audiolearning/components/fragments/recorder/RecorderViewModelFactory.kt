package com.example.audiolearning.components.fragments.recorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.audiolearning.audio.audio_recorder.AudioRecorder
import com.example.audiolearning.audio.audio_recorder.IAudioRecorder
import com.example.audiolearning.audio.audio_store.IAudioStore
import com.example.audiolearning.util.timer.ITimer
import com.example.audiolearning.util.timer.Timer

class RecorderViewModelFactory(
    private val audioRecorder: IAudioRecorder = AudioRecorder(),
    private val timer: ITimer = Timer(),
    private val audioStore: IAudioStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecorderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecorderViewModel(audioRecorder, timer, audioStore) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}