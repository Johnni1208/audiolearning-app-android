package com.audiolearning.app.ui.dialog.createnewsubject

import androidx.lifecycle.ViewModel
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.extension.isAllowedFileName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Suppress("ReturnCount")
class CreateNewSubjectDialogViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository
) : ViewModel() {
    suspend fun createNewSubject(subjectName: String) =
        withContext(Dispatchers.IO) { subjectRepository.insert(subjectName) }

    suspend fun validateInput(subjectName: String): CreateNewSubjectInputValidation {
        if (subjectName.isEmpty()) {
            return CreateNewSubjectInputValidation.INPUT_FIELD_IS_BLANK
        }

        if (!subjectName.isAllowedFileName()) {
            return CreateNewSubjectInputValidation.INPUT_FIELD_CONTAINS_INVALID_CHARS
        }

        if (getSubjectByName(subjectName) != null) {
            return CreateNewSubjectInputValidation.SUBJECT_ALREADY_EXISTS
        }

        return CreateNewSubjectInputValidation.CORRECT
    }

    private suspend fun getSubjectByName(subjectName: String) = withContext(Dispatchers.IO) {
        subjectRepository.getSubjectByName(subjectName)
    }
}
