package com.example.audiolearning.ui.dialogs.create_new_subject

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.audiolearning.R
import com.example.audiolearning.data.db.entities.Subject
import com.example.audiolearning.data.repositories.SubjectRepository
import com.example.audiolearning.extensions.isAllowedFileName
import com.example.audiolearning.util.SubjectFileUtils
import kotlinx.coroutines.runBlocking
import java.io.File

class CreateNewSubjectDialogViewModel(
    private val subjectRepository: SubjectRepository
) : ViewModel() {

    private val _error = MutableLiveData<Int>().apply {
        value = null
    }
    val error: LiveData<Int>
        get() = _error

    fun createNewSubject(filesDir: File, subjectName: String): Boolean {
        if (subjectName.isEmpty()) {
            _error.value = R.string.dialog_error_message_missing_info
            return false
        }

        if (!subjectName.isAllowedFileName()) {
            _error.value = R.string.dialog_error_message_contains_not_allowed_character
            return false
        }

        var isSubjectSaved = false
        runBlocking {
            isSubjectSaved = saveSubject(filesDir, subjectName)
        }

        return isSubjectSaved
    }

    suspend fun saveSubject(filesDir: File, subjectName: String): Boolean {
        val subjectAlreadyExists = subjectRepository.getSubjectByName(subjectName) != null
        if (subjectAlreadyExists) {
            _error.value = R.string.cnsDialog_error_subject_already_exists
            return false
        }

        val subjectDir = SubjectFileUtils.createNewSubjectDirectory(filesDir, subjectName)

        val subject = Subject(subjectName, Uri.fromFile(subjectDir).toString())

        subjectRepository.upsert(subject)

        return true
    }
}