package com.example.audiolearning.ui.dialogs.new_recording

import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.example.audiolearning.data.db.entities.Subject
import com.example.audiolearning.data.repositories.AudioRepository
import com.example.audiolearning.data.repositories.SubjectRepository
import com.example.audiolearning.extensions.isAllowedFileName
import com.example.audiolearning.ui.dialogs.create_new_subject.CreateNewSubjectDialog
import java.io.File

class NewRecordingDialogViewModel(
    private val subjectRepository: SubjectRepository,
    private val audioRepository: AudioRepository
) : ViewModel() {

    fun getSubjects() = subjectRepository.getAllSubjects()

    suspend fun saveAudio(file: File, name: String, subject: Subject) =
        audioRepository.insert(file, name, subject)

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
     * This method returns an itemSelectListener, which opens an CreateNewSubjectDialog
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
}