package com.audiolearning.app.ui.activities

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.ui.activities.subject.SubjectActivityViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SubjectActivityViewModelTest {
    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SubjectActivityViewModel
    private val mockRepository: SubjectRepository = mock()
    private val testSubject = Subject("testName", "")

    @Before
    fun setup() {
        viewModel = SubjectActivityViewModel(mockRepository)

    }

    @Test
    fun setTitleToSubjectName_ShouldSetTitleOfSubject() = runBlocking {
        whenever(mockRepository.getSubjectById(1)).thenReturn(testSubject)

        viewModel.setTitleToSubjectName(1)

        assertEquals(testSubject.name, viewModel.title.value)
    }
}