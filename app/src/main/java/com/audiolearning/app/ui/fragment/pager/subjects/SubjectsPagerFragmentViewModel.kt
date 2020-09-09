package com.audiolearning.app.ui.fragment.pager.subjects

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.repositories.AudioRepository
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.data.store.SelectedEntityStore
import com.audiolearning.app.extension.id
import com.audiolearning.app.service.audioplayer.AudioPlayerServiceConnection
import com.audiolearning.app.util.NO_ID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SubjectsPagerFragmentViewModel @ViewModelInject constructor(
    private val subjectRepository: SubjectRepository,
    private val audioRepository: AudioRepository,
    private val selectedSubjectStore: SelectedEntityStore<Subject>,
    private val audioPlayerServiceConnection: AudioPlayerServiceConnection
) : ViewModel() {
    val selectedSubjectsList: LiveData<ArrayList<Subject>> = selectedSubjectStore.selectedEntityList

    val subjects: LiveData<List<Subject>>
        get() = subjectRepository.getAllSubjects()

    fun selectSubject(subject: Subject) = selectedSubjectStore.select(subject)

    fun deselectSubject(subject: Subject) = selectedSubjectStore.deselect(subject)

    fun deselectAllSubjects() = selectedSubjectStore.clear()

    suspend fun deleteAllSelectedSubjects() {
        stopPlayingIfAudioIsDeleted()
        deleteSelectedSubjectsFromRepository()
        deselectAllSubjects()
    }

    private suspend fun deleteSelectedSubjectsFromRepository() = withContext(Dispatchers.IO) {
        selectedSubjectsList.value?.forEach { selectedSubject ->
            subjectRepository.delete(selectedSubject)
        }
    }

    private suspend fun stopPlayingIfAudioIsDeleted() {
        val nowPlayingAudioId: Int = audioPlayerServiceConnection.nowPlaying.value?.id!!.toInt()

        // Get all audios of selected Subject
        val selectedAudios: ArrayList<Audio> = ArrayList<Audio>().apply {
            selectedSubjectsList.value?.forEach { selectedSubject ->
                addAll(getAudiosOfSubject(selectedSubject.id ?: NO_ID))
            }
        }

        selectedAudios.forEach { audio ->
            if (nowPlayingAudioId == audio.id) {
                audioPlayerServiceConnection.transportControls.stop()
            }
        }
    }

    private suspend fun getAudiosOfSubject(subjectId: Int) = withContext(Dispatchers.IO) {
        return@withContext audioRepository.getAudiosOfSubject(subjectId)
    }
}
