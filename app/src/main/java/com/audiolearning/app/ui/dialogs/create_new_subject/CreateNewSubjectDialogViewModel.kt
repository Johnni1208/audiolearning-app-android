package com.audiolearning.app.ui.dialogs.create_new_subject

import androidx.lifecycle.ViewModel
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.extensions.isAllowedFileName
import kotlinx.coroutines.runBlocking

class CreateNewSubjectDialogViewModel(
    private val subjectRepository: SubjectRepository
) : ViewModel() {
    suspend fun createNewSubject(subjectName: String) = subjectRepository.insert(subjectName)

    fun validateInput(subjectName: String): CreateNewSubjectInputValidation {
        if (subjectName.isEmpty()) {
            return CreateNewSubjectInputValidation.INPUT_FIELD_IS_BLANK
        }

        if (!subjectName.isAllowedFileName()) {
            return CreateNewSubjectInputValidation.INPUT_FIELD_CONTAINS_INVALID_CHARS
        }

        var subjectAlreadyExists = false
        runBlocking {
            subjectAlreadyExists = subjectRepository.getSubjectByName(subjectName) != null
        }
        if (subjectAlreadyExists) {
            return CreateNewSubjectInputValidation.SUBJECT_ALREADY_EXISTS
        }

        return CreateNewSubjectInputValidation.CORRECT
    }
}