package com.audiolearning.app.ui.fragment.audiosofsubject

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.repositories.AudioRepository
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.data.store.SelectedEntityStore
import com.audiolearning.app.extension.id
import com.audiolearning.app.service.audioplayer.AudioPlayerServiceConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AudiosOfSubjectFragmentViewModel @ViewModelInject constructor(
    private val subjectRepository: SubjectRepository,
    private val audioRepository: AudioRepository,
    private val selectedAudiosStore: SelectedEntityStore<Audio>,
    private val audioPlayerServiceConnection: AudioPlayerServiceConnection
) : ViewModel() {
    val selectedAudiosList: LiveData<ArrayList<Audio>> = selectedAudiosStore.selectedEntityList

    val audios: LiveData<List<Audio>>
        get() = audioRepository.getAudiosOfSubjectLiveData(subject.value?.id!!)

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
        stopPlayingIfAudioIsDeleted()
        deleteSelectedAudiosFromRepository()
        deselectAllAudios()
    }

    private suspend fun deleteSelectedAudiosFromRepository() = withContext(Dispatchers.IO) {
        selectedAudiosList.value?.forEach { selectedAudio ->
            audioRepository.delete(selectedAudio)
        }
    }

    private fun stopPlayingIfAudioIsDeleted() {
        val nowPlayingAudioId: Int = audioPlayerServiceConnection.nowPlaying.value?.id!!.toInt()

        selectedAudiosList.value?.forEach { audio ->
            if (nowPlayingAudioId == audio.id) audioPlayerServiceConnection.transportControls.stop()
        }
    }
}
