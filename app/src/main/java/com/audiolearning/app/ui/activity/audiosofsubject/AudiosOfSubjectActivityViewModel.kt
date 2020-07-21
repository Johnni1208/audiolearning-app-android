package com.audiolearning.app.ui.activity.audiosofsubject

import android.os.Bundle
import androidx.core.net.toUri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.repositories.AudioRepository
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.data.store.SelectedEntityStore
import com.audiolearning.app.extension.from
import com.audiolearning.app.extension.id
import com.audiolearning.app.extension.isPlayEnabled
import com.audiolearning.app.extension.isPlaying
import com.audiolearning.app.extension.isPrepared
import com.audiolearning.app.service.audioplayer.AudioPlayerServiceConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class AudiosOfSubjectActivityViewModel @ViewModelInject constructor(
    private val subjectRepository: SubjectRepository,
    private val audioRepository: AudioRepository,
    private val selectedAudiosStore: SelectedEntityStore<Audio>,
    private val audioPlayerServiceConnection: AudioPlayerServiceConnection
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

    fun playAudio(audio: Audio) {
        val nowPlaying = audioPlayerServiceConnection.nowPlaying.value
        val transportControls = audioPlayerServiceConnection.transportControls

        val isPrepared = audioPlayerServiceConnection.playBackState.value?.isPrepared ?: false
        if (isPrepared && audio.id!! == nowPlaying?.id?.toInt()) {
            audioPlayerServiceConnection.playBackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> transportControls.pause()
                    playbackState.isPlayEnabled -> transportControls.play()
                    else -> {
                        Timber.w(
                            """Playable item clicked but neither play nor pause are enabled! (mediaId=${audio.id})"""
                        )
                    }
                }
            }
        } else {
            subject.value?.let {
                transportControls.playFromUri(audio.fileUriString.toUri(), Bundle().from(audio, it))
            }
        }
    }

    suspend fun playAudioId(audioId: String) {
        val nowPlaying = audioPlayerServiceConnection.nowPlaying.value
        val transportControls = audioPlayerServiceConnection.transportControls

        val isPrepared = audioPlayerServiceConnection.playBackState.value?.isPrepared ?: false
        if (isPrepared && audioId == nowPlaying?.id) {
            audioPlayerServiceConnection.playBackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> transportControls.pause()
                    playbackState.isPlayEnabled -> transportControls.play()
                    else -> {
                        Timber.w(
                            """Playable item clicked but neither play nor pause are enabled! (mediaId=$audioId)"""
                        )
                    }
                }
            }
        } else {
            val audio = getAudioById(audioId.toInt())
            val subject = getSubjectById(audio.subjectId)

            transportControls.playFromUri(
                audio.fileUriString.toUri(),
                Bundle().from(audio, subject)
            )
        }
    }

    private suspend fun getAudioById(id: Int) = withContext(Dispatchers.IO) {
        return@withContext audioRepository.getAudioById(id)
            ?: throw IllegalArgumentException("No audio with id: $id")
    }
}
