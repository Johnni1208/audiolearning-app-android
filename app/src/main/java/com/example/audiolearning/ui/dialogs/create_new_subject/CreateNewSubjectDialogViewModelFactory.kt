package com.example.audiolearning.ui.dialogs.create_new_subject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.audiolearning.data.repositories.SubjectRepository

class CreateNewSubjectDialogViewModelFactory(
    private val subjectRepository: SubjectRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateNewSubjectDialogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateNewSubjectDialogViewModel(subjectRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}