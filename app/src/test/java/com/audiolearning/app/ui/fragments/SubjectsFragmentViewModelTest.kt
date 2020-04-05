package com.audiolearning.app.ui.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.ui.fragments.subjects.SubjectsFragmentViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SubjectsFragmentViewModelTest {
    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testSubject = Subject("testSubject", "testDir")
    private val mockRepository: SubjectRepository = mock()
    private lateinit var fragmentViewModel: SubjectsFragmentViewModel

    @Before
    fun setup() {
        fragmentViewModel = SubjectsFragmentViewModel(mockRepository)
    }

    @Test
    fun getSubjects_ShouldCallRepository() {
        fragmentViewModel.getSubjects()

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
    fun selectSubjectItem_ShouldAddItemToSelectedList() {
        assertTrue(fragmentViewModel.selectSubject(testSubject))
        assertEquals(testSubject, fragmentViewModel.selectedSubjectsList.value?.first())
    }

    @Test
    fun selectSubjectItem_ShouldNotAddItemToListIfItIsAlreadyInIt() {
        fragmentViewModel.selectSubject(testSubject)

        assertFalse(fragmentViewModel.selectedSubjectsList.value?.size!! > 1)
        assertFalse(fragmentViewModel.selectSubject(testSubject))
    }

    @Test
    fun deselectSubjectItem_ShouldRemoveSubjectFromSelectedList() {
        fragmentViewModel.selectSubject(testSubject)

        assertTrue(fragmentViewModel.deselectSubject(testSubject))
        assertFalse(fragmentViewModel.selectedSubjectsList.value?.contains(testSubject)!!)
    }

    @Test
    fun deselectSubjectItem_ShouldNotRemoveSubjectIfItIsNotInTheList() {
        val secondTestSubject = Subject("testSubject2", "")

        fragmentViewModel.selectSubject(secondTestSubject)

        assertFalse(fragmentViewModel.deselectSubject(testSubject))
        assertTrue(fragmentViewModel.selectedSubjectsList.value?.size!! == 1)
    }

    @Test
    fun deleteAllSelectedSubjects_ShouldCallRepositoryDelete() = runBlocking {
        fragmentViewModel.selectSubject(testSubject)

        fragmentViewModel.deleteAllSelectedSubjects()

        verify(mockRepository).delete(testSubject)
    }

    @Test
    fun deleteALlSelectedSubjects_ShouldClearSelectedSubjectsList() = runBlocking {
        fragmentViewModel.selectSubject(testSubject)

        fragmentViewModel.deleteAllSelectedSubjects()

        assertTrue(fragmentViewModel.selectedSubjectsList.value?.isEmpty()!!)
    }

    @Test
    fun deselectAllSubjects_ShouldEmptySelectedSubjectsList() {
        fragmentViewModel.selectSubject(testSubject)

        fragmentViewModel.deselectAllSubjects()

        assertTrue(fragmentViewModel.selectedSubjectsList.value?.isEmpty()!!)
    }
}