package com.audiolearning.app.ui.dialogs.new_recording

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.repositories.AudioRepository
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.extensions.isAllowedFileName
import com.audiolearning.app.ui.dialogs.create_new_subject.CreateNewSubjectDialog
import com.audiolearning.app.util.MissingArgumentException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class NewRecordingDialogViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val audioRepository: AudioRepository
) : ViewModel() {
    fun getSubjects() = subjectRepository.getAllSubjects()

    suspend fun saveAudio(file: File, name: String, subject: Subject) =
        withContext(Dispatchers.IO) {
            audioRepository.insert(file, name, subject)
        }

    fun validateInput(name: String, subject: Subject): NewRecordingInputValidation {
        if (name.isEmpty()) {
            return NewRecordingInputValidation.NAME_IS_BLANK
        }

        if (!name.isAllowedFileName()) {
            return NewRecordingInputValidation.NAME_CONTAINS_INVALID_CHARS
        }

        if (!subject.isRealSubject) {
            return NewRecordingInputValidation.SUBJECT_IS_BLANK
        }

        return NewRecordingInputValidation.CORRECT
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
            }
        }

    fun receiveNewRecordingFromArguments(args: Bundle): File {
        val newRecordingFilePath = args.getString(NewRecordingDialog.ARG_NEW_FILE_PATH)
            ?: throw MissingArgumentException(NewRecordingDialog.ARG_NEW_FILE_PATH)
        if (newRecordingFilePath.isEmpty()) throw MissingArgumentException(NewRecordingDialog.ARG_NEW_FILE_PATH)

        return File(newRecordingFilePath)
    }
}