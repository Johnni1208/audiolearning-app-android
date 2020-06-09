package com.audiolearning.app.ui.activities.audios_of_subject

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
import javax.inject.Inject

class AudiosOfSubjectActivityViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val audioRepository: AudioRepository,
    private val selectedAudiosStore: SelectedEntityStore<Audio>
) : ViewModel() {
    val selectedAudiosList: LiveData<ArrayList<Audio>> = selectedAudiosStore.selectedEntityList

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

    suspend fun getAudioById(id: Int) = withContext(Dispatchers.IO) {
        return@withContext audioRepository.getAudioById(id)
            ?: throw IllegalArgumentException("No audio with id: $id")
    }

    fun getAudios() = audioRepository.getAudiosOfSubject(subject.value?.id!!)

    fun selectAudio(audio: Audio) = selectedAudiosStore.select(audio)

    fun deselectAudio(audio: Audio) = selectedAudiosStore.deselect(audio)

    fun deselectAllAudios() = selectedAudiosStore.clear()

    suspend fun deleteAllSelectedAudios() {
        deleteSelectedAudiosFromDb()

        selectedAudiosStore.clear()
    }

    private suspend fun deleteSelectedAudiosFromDb() = withContext(Dispatchers.IO) {
        selectedAudiosStore.selectedEntityList.value?.forEach { selectedAudio ->
            audioRepository.delete(selectedAudio)
        }
    }
}