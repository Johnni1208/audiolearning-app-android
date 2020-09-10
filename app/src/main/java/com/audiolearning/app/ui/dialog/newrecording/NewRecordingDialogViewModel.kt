package com.audiolearning.app.ui.dialog.newrecording

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.FragmentManager
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.repositories.AudioRepository
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.exception.MissingArgumentException
import com.audiolearning.app.extension.isAllowedFileName
import com.audiolearning.app.ui.dialog.createnewsubject.CreateNewSubjectDialog
import com.audiolearning.app.util.NO_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@Suppress("ReturnCount")
class NewRecordingDialogViewModel @ViewModelInject constructor(
    private val subjectRepository: SubjectRepository,
    private val audioRepository: AudioRepository
) : ViewModel() {
    fun getSubjects() = subjectRepository.getAllSubjects()

    suspend fun saveAudio(file: File, name: String, subject: Subject) =
        withContext(Dispatchers.IO) {
            audioRepository.insert(file, name, subject)
        }

    suspend fun validateInput(name: String, subject: Subject): NewRecordingInputValidation {
        if (name.isEmpty()) {
            return NewRecordingInputValidation.NAME_IS_BLANK
        }

        if (!name.isAllowedFileName()) {
            return NewRecordingInputValidation.NAME_CONTAINS_INVALID_CHARS
        }

        if (!subject.isRealSubject) {
            return NewRecordingInputValidation.SUBJECT_IS_BLANK
        }

        getAudiosOfSubject(subject.id ?: NO_ID).forEach { audio ->
            if (name == audio.name) return NewRecordingInputValidation.NAME_ALREADY_EXISTS_IN_SUBJECT
        }

        return NewRecordingInputValidation.CORRECT
    }

    private suspend fun getAudiosOfSubject(subjectId: Int) = withContext(Dispatchers.IO) {
        return@withContext audioRepository.getAudiosOfSubject(subjectId)
    }

    /**
     * This method returns an itemSelectListener, which opens an [CreateNewSubjectDialog]
     * when the "Add new subject..." item is selected. (Has to be in first place of the spinner!)
     */
    fun getAddHintItemSelectedListener(fragmentManager: FragmentManager) =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    CreateNewSubjectDialog().show(
                        fragmentManager,
                        "NewRecordingDialog"
                    )
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Nothing should happen, if nothing is selected
            }
        }

    fun receiveNewRecordingFromArguments(args: Bundle): File {
        val newRecordingFilePath = args.getString(NewRecordingDialog.ARG_NEW_FILE_PATH)
            ?: throw MissingArgumentException(
                NewRecordingDialog.ARG_NEW_FILE_PATH
            )
        if (newRecordingFilePath.isEmpty()) throw MissingArgumentException(
            NewRecordingDialog.ARG_NEW_FILE_PATH
        )

        return File(newRecordingFilePath)
    }
}
