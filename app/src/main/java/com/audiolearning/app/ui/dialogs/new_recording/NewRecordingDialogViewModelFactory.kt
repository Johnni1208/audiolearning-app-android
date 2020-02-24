package com.audiolearning.app.ui.dialogs.new_recording

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.audiolearning.app.data.repositories.AudioRepository
import com.audiolearning.app.data.repositories.SubjectRepository

class NewRecordingDialogViewModelFactory(
    private val subjectRepository: SubjectRepository,
    private val audioRepository: AudioRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewRecordingDialogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewRecordingDialogViewModel(subjectRepository, audioRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}