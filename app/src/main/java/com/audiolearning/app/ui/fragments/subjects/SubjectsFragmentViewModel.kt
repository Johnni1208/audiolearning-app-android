package com.audiolearning.app.ui.fragments.subjects

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.data.store.SelectedEntityStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SubjectsFragmentViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository,
    private val selectedSubjectStore: SelectedEntityStore<Subject>
) : ViewModel() {
    val selectedSubjectsList: LiveData<ArrayList<Subject>> = selectedSubjectStore.selectedEntityList

    fun getSubjects() = subjectRepository.getAllSubjects()

    suspend fun getSubjectById(id: Int) = withContext(Dispatchers.IO) {
        subjectRepository.getSubjectById(id)
            ?: throw IllegalArgumentException("Could not find subject with id $id")
    }

    fun selectSubject(subject: Subject) = selectedSubjectStore.select(subject)

    fun deselectSubject(subject: Subject) = selectedSubjectStore.deselect(subject)

    fun deselectAllSubjects() = selectedSubjectStore.clear()

    suspend fun deleteAllSelectedSubjects() {
        deleteSelectedSubjectsFromDb()

        selectedSubjectStore.clear()
    }

    private suspend fun deleteSelectedSubjectsFromDb() = withContext(Dispatchers.IO) {
        selectedSubjectStore.selectedEntityList.value?.forEach { selectedSubject ->
            subjectRepository.delete(selectedSubject)
        }
    }
}