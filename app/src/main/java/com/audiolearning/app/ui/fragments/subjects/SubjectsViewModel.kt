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
    private var mutableSubjectSelectList: ArrayList<Subject> = arrayListOf()
    private val _subjectsSelectedList = MutableLiveData<ArrayList<Subject>>().apply {
        value = mutableSubjectSelectList
    }

    val subjectsSelectedList: LiveData<ArrayList<Subject>>
        get() = _subjectsSelectedList

    fun getSubjects() = subjectRepository.getAllSubjects()

    suspend fun getSubjectById(id: Int) = withContext(Dispatchers.IO) {
        subjectRepository.getSubjectById(id)
            ?: throw IllegalArgumentException("Could not find subject with id $id")
    }

    fun selectSubjectItem(subject: Subject): Boolean {
        if (mutableSubjectSelectList.addIfNotContained(subject)) {
            _subjectsSelectedList.postValue(mutableSubjectSelectList)
            return true
        }

        return false
    }

    fun deselectSubjectItem(subject: Subject): Boolean {
        if (mutableSubjectSelectList.removeIfContained(subject)) {
            _subjectsSelectedList.postValue(mutableSubjectSelectList)
            return true
        }

        return false
    }

    suspend fun deleteAllSelectedSubjects() {
        deleteSelectedSubjectsFromDb()

        mutableSubjectSelectList.clear()
        _subjectsSelectedList.postValue(mutableSubjectSelectList)
    }

    private suspend fun deleteSelectedSubjectsFromDb() = withContext(Dispatchers.IO) {
        mutableSubjectSelectList.forEach { selectedSubject ->
            subjectRepository.delete(selectedSubject)
        }
    }
}