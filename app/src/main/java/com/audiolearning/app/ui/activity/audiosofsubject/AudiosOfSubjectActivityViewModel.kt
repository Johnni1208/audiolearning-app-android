package com.audiolearning.app.ui.activity.audiosofsubject

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.repositories.AudioRepository
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.data.store.SelectedEntityStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class AudiosOfSubjectActivityViewModel @ViewModelInject constructor(
    private val subjectRepository: SubjectRepository,
    private val audioRepository: AudioRepository,
    private val selectedAudiosStore: SelectedEntityStore<Audio>
) : ViewModel() {
    val selectedAudiosList: LiveData<ArrayList<Audio>> = selectedAudiosStore.selectedEntityList

    val audios: LiveData<List<Audio>>
        get() = audioRepository.getAudiosOfSubject(subject.value?.id!!)

    private val _subject: MutableLiveData<Subject> = MutableLiveData<Subject>().apply {
        value = null
    }

    val subject: LiveData<Subject>
        get() = _subject

    suspend fun setSubject(id: Int) {
        _subject.value = getSubjectById(id)
    }

    private suspend fun getSubjectById(id: Int) = withContext(Dispatchers.IO) {
        return@withContext subjectRepository.getSubjectById(id)
            ?: throw IllegalArgumentException("No subject with id: $id")
    }

    fun selectAudio(audio: Audio) = selectedAudiosStore.select(audio)

    fun deselectAudio(audio: Audio) = selectedAudiosStore.deselect(audio)

    fun deselectAllAudios() = selectedAudiosStore.clear()

    suspend fun deleteAllSelectedAudios() {
        deleteSelectedAudiosFromRepository()

        selectedAudiosStore.clear()
    }

    private suspend fun deleteSelectedAudiosFromRepository() = withContext(Dispatchers.IO) {
        selectedAudiosStore.selectedEntityList.value?.forEach { selectedAudio ->
            audioRepository.delete(selectedAudio)
        }
    }
}
