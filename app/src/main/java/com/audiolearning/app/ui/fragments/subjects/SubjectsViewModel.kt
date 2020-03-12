package com.audiolearning.app.ui.fragments.subjects

import androidx.lifecycle.ViewModel
import com.audiolearning.app.data.repositories.SubjectRepository
import javax.inject.Inject

class SubjectsViewModel @Inject constructor(private val subjectRepository: SubjectRepository) :
    ViewModel() {
    fun getSubjects() = subjectRepository.getAllSubjects()
}