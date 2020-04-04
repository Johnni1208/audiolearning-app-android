package com.audiolearning.app.ui.fragments.subjects

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.extensions.addIfNotContained
import com.audiolearning.app.extensions.removeIfContained
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SubjectsViewModel @Inject constructor(private val subjectRepository: SubjectRepository) :
    ViewModel() {
    private var mutableSelectedSubjectsList: ArrayList<Subject> = arrayListOf()
    private val _selectedSubjectsList = MutableLiveData<ArrayList<Subject>>().apply {
        value = arrayListOf()
    }

    val selectedSubjectsList: LiveData<ArrayList<Subject>>
        get() = _selectedSubjectsList

    fun getSubjects() = subjectRepository.getAllSubjects()

    suspend fun getSubjectById(id: Int) = withContext(Dispatchers.IO) {
        subjectRepository.getSubjectById(id)
            ?: throw IllegalArgumentException("Could not find subject with id $id")
    }

    fun selectSubject(subject: Subject): Boolean {
        if (mutableSelectedSubjectsList.addIfNotContained(subject)) {
            _selectedSubjectsList.postValue(mutableSelectedSubjectsList)
            return true
        }

        return false
    }

    fun deselectSubject(subject: Subject): Boolean {
        if (mutableSelectedSubjectsList.removeIfContained(subject)) {
            _selectedSubjectsList.postValue(mutableSelectedSubjectsList)
            return true
        }

        return false
    }

    suspend fun deleteAllSelectedSubjects() {
        deleteSelectedSubjectsFromDb()

        mutableSelectedSubjectsList.clear()
        _selectedSubjectsList.postValue(mutableSelectedSubjectsList)
    }

    private suspend fun deleteSelectedSubjectsFromDb() = withContext(Dispatchers.IO) {
        mutableSelectedSubjectsList.forEach { selectedSubject ->
            subjectRepository.delete(selectedSubject)
        }
    }

    fun deselectAllSubjects() {
        mutableSelectedSubjectsList.clear()
        _selectedSubjectsList.postValue(mutableSelectedSubjectsList)
    }
}