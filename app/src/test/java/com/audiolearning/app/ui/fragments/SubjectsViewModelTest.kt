package com.audiolearning.app.ui.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.ui.fragments.subjects.SubjectsViewModel
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
class SubjectsViewModelTest {
    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testSubject = Subject("testSubject", "testDir")
    private val mockRepository: SubjectRepository = mock()
    private lateinit var viewModel: SubjectsViewModel

    @Before
    fun setup() {
        viewModel = SubjectsViewModel(mockRepository)
    }

    @Test
    fun getSubjects_ShouldCallRepository() {
        viewModel.getSubjects()

        verify(mockRepository).getAllSubjects()
    }

    @Test
    fun getSubjectById_ShouldReturnSubject() = runBlocking {
        val subjectName = "testSubject"
        val subjectId = 0
        val testSubject = Subject(subjectName, "testDir").apply { id = 0 }
        whenever(mockRepository.getSubjectById(subjectId)).thenReturn(testSubject)

        val returnedSubject: Subject = viewModel.getSubjectById(subjectId)
        assertTrue(testSubject == returnedSubject)
    }

    @Suppress("UNUSED_VARIABLE")
    @Test(expected = IllegalArgumentException::class)
    fun getSubjectById_ShouldThrowError_IfSubjectIsNotFound() = runBlocking {
        val returnedSubject = viewModel.getSubjectById(1)
    }

    @Test
    fun selectSubjectItem_ShouldAddItemToSelectedList() {
        assertTrue(viewModel.selectSubject(testSubject))
        assertEquals(testSubject, viewModel.selectedSubjectsList.value?.first())
    }

    @Test
    fun selectSubjectItem_ShouldNotAddItemToListIfItIsAlreadyInIt() {
        viewModel.selectSubject(testSubject)

        assertFalse(viewModel.selectedSubjectsList.value?.size!! > 1)
        assertFalse(viewModel.selectSubject(testSubject))
    }

    @Test
    fun deselectSubjectItem_ShouldRemoveSubjectFromSelectedList() {
        viewModel.selectSubject(testSubject)

        assertTrue(viewModel.deselectSubject(testSubject))
        assertFalse(viewModel.selectedSubjectsList.value?.contains(testSubject)!!)
    }

    @Test
    fun deselectSubjectItem_ShouldNotRemoveSubjectIfItIsNotInTheList() {
        val secondTestSubject = Subject("testSubject2", "")

        viewModel.selectSubject(secondTestSubject)

        assertFalse(viewModel.deselectSubject(testSubject))
        assertTrue(viewModel.selectedSubjectsList.value?.size!! == 1)
    }

    @Test
    fun deleteAllSelectedSubjects_ShouldCallRepositoryDelete() = runBlocking {
        viewModel.selectSubject(testSubject)

        viewModel.deleteAllSelectedSubjects()

        verify(mockRepository).delete(testSubject)
    }

    @Test
    fun deleteALlSelectedSubjects_ShouldClearSelectedSubjectsList() = runBlocking {
        viewModel.selectSubject(testSubject)

        viewModel.deleteAllSelectedSubjects()

        assertTrue(viewModel.selectedSubjectsList.value?.isEmpty()!!)
    }

    @Test
    fun deselectAllSubjects_ShouldEmptySelectedSubjectsList() {
        viewModel.selectSubject(testSubject)

        viewModel.deselectAllSubjects()

        assertTrue(viewModel.selectedSubjectsList.value?.isEmpty()!!)
    }
}