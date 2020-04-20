package com.audiolearning.app.ui.activities.subject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.repositories.AudioRepository
import com.audiolearning.app.data.repositories.SubjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SubjectActivityViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val audioRepository: AudioRepository
) : ViewModel() {
    private val _subject = MutableLiveData<Subject>().apply {
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

    fun getAudios() = audioRepository.getAudiosOfSubject(subject.value?.id!!)
}