package com.example.audiolearning.ui.dialogs.new_recording

import android.net.Uri
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.example.audiolearning.data.db.entities.Audio
import com.example.audiolearning.data.db.entities.Subject
import com.example.audiolearning.data.repositories.AudioRepository
import com.example.audiolearning.data.repositories.SubjectRepository
import com.example.audiolearning.ui.dialogs.create_new_subject.CreateNewSubjectDialog
import com.example.audiolearning.util.AudioFileUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class NewRecordingDialogViewModel(
    private val subjectRepository: SubjectRepository,
    private val audioRepository: AudioRepository
) : ViewModel() {

    suspend fun getSubjects() = subjectRepository.getAllSubjects()

    fun getSubjectSpinnerOnItemSelectedListener(fragmentManager: FragmentManager) =
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

    fun saveAudio(file: File, name: String, subject: Subject) {
        val subjectDirectory = Uri.parse(subject.directoryUriString).path!!

        AudioFileUtils.cutFileAndPasteToDirectory(
            file,
            subjectDirectory,
            name
        )

        val audioFile =
            File(subjectDirectory + File.separatorChar + name + Audio.fileExtension)
        val audioFileUri = Uri.fromFile(audioFile)

        GlobalScope.launch {
            audioRepository.upsert(Audio(name, subject.id!!, audioFileUri.toString()))
        }
    }
}