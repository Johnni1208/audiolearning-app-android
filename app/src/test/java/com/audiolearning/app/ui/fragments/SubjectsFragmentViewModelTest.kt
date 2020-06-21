package com.audiolearning.app.ui.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.data.store.SelectedEntityStore
import com.audiolearning.app.ui.fragments.subjects.SubjectsFragmentViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SubjectsFragmentViewModelTest {
    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testSubject = Subject("testSubject", "testDir")
    private val mockRepository: SubjectRepository = mock()
    private lateinit var selectedEntityStore: SelectedEntityStore<Subject>
    private lateinit var fragmentViewModel: SubjectsFragmentViewModel

    @Before
    fun setup() {
        selectedEntityStore = SelectedEntityStore()
        fragmentViewModel = SubjectsFragmentViewModel(mockRepository, selectedEntityStore)
    }

    @Test
    fun getSubjects_ShouldCallRepository() {
        fragmentViewModel.subjects

        verify(mockRepository).getAllSubjects()
    }

    @Test
    fun getSubjectById_ShouldReturnSubject() = runBlocking {
        val subjectName = "testSubject"
        val subjectId = 0
        val testSubject = Subject(subjectName, "testDir").apply { id = 0 }
        whenever(mockRepository.getSubjectById(subjectId)).thenReturn(testSubject)

        val returnedSubject: Subject = fragmentViewModel.getSubjectById(subjectId)
        assertTrue(testSubject == returnedSubject)
    }

    @Suppress("UNUSED_VARIABLE")
    @Test(expected = IllegalArgumentException::class)
    fun getSubjectById_ShouldThrowError_IfSubjectIsNotFound() = runBlocking {
        val returnedSubject = fragmentViewModel.getSubjectById(1)
    }

    @Test
    fun deleteAllSelectedSubjects_ShouldCallRepositoryDelete() = runBlocking {
        fragmentViewModel.selectSubject(testSubject)

        fragmentViewModel.deleteAllSelectedSubjects()

        verify(mockRepository).delete(testSubject)
    }

    @Test
    fun deleteAllSelectedSubjects_ShouldClearSelectedSubjectsList() = runBlocking {
        fragmentViewModel.selectSubject(testSubject)

        fragmentViewModel.deleteAllSelectedSubjects()

        assertTrue(fragmentViewModel.selectedSubjectsList.value?.isEmpty()!!)
    }
}