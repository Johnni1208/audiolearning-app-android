package com.example.audiolearning.ui.fragments.recorder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.audiolearning.audio.audio_recorder.AudioRecorder
import com.example.audiolearning.audio.audio_recorder.IAudioRecorder
import com.example.audiolearning.data.repositories.AudioRepository
import com.example.audiolearning.util.timer.ITimer
import com.example.audiolearning.util.timer.Timer

class RecorderViewModelFactory(
    private val audioRecorder: IAudioRecorder = AudioRecorder(),
    private val timer: ITimer = Timer(),
    private val audioRepository: AudioRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecorderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecorderViewModel(audioRecorder, timer, audioRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

}