package com.audiolearning.app.ui.activity.audioplayer

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.repositories.AudioRepository
import com.audiolearning.app.data.repositories.SubjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AudioPlayerActivityViewModel @ViewModelInject constructor(
    private val audioRepository: AudioRepository,
    private val subjectRepository: SubjectRepository
) : ViewModel() {
    private val _audio: MutableLiveData<Audio> = MutableLiveData<Audio>().apply {
        value = null
    }

    val audio: LiveData<Audio>
        get() = _audio

    private val _subject: MutableLiveData<Subject> = MutableLiveData<Subject>().apply {
        value = null
    }
    val subject: LiveData<Subject>
        get() = _subject

    suspend fun setAudio(id: Int) {
        _audio.value = getAudioById(id)
        _subject.value = getSubject()
    }

    private suspend fun getAudioById(id: Int) = withContext(Dispatchers.IO) {
        return@withContext audioRepository.getAudioById(id)
            ?: throw IllegalArgumentException("No audio with id: $id")
    }

    private suspend fun getSubject() = withContext(Dispatchers.IO) {
        return@withContext subjectRepository.getSubjectById(audio.value!!.subjectId)
    }
}
