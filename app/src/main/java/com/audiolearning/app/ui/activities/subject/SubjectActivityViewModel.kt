package com.audiolearning.app.ui.activities.subject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audiolearning.app.data.repositories.SubjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SubjectActivityViewModel @Inject constructor(private val subjectRepository: SubjectRepository) :
    ViewModel() {
    private val _title = MutableLiveData<String>().apply {
        value = ""
    }
    val title: LiveData<String>
        get() = _title

    private val _transitionName = MutableLiveData<String>().apply {
        value = ""
    }
    val transitionName: LiveData<String>
        get() = _transitionName

    suspend fun setTitleToSubjectName(id: Int) {
        _title.postValue(getSubjectById(id).name)
    }

    private suspend fun getSubjectById(id: Int) = withContext(Dispatchers.IO) {
        return@withContext subjectRepository.getSubjectById(id)
            ?: throw IllegalArgumentException("No subject with id: $id")
    }

    fun setTransitionName(name: String) {
        _transitionName.value = name
    }
}