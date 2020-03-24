package com.audiolearning.app.ui.dialogs

import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.ui.dialogs.create_new_subject.CreateNewSubjectDialogViewModel
import com.audiolearning.app.ui.dialogs.create_new_subject.CreateNewSubjectInputValidation
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CreateNewSubjectDialogViewModelTest {
    private lateinit var viewModel: CreateNewSubjectDialogViewModel
    private lateinit var mockSubjectRepository: SubjectRepository

    private val testSubjectName = "testSubject"

    @Before
    fun setUp() {
        mockSubjectRepository = mock()
        viewModel = CreateNewSubjectDialogViewModel(mockSubjectRepository)
    }

    @Test
    fun createNewSubject_ShouldCallSubjectRepositoryInsert() = runBlocking {
        viewModel.createNewSubject(testSubjectName)
        verify(mockSubjectRepository).insert(testSubjectName)
    }

    @Test
    fun validateInput_ShouldReturnFIELD_IS_BLANK_IfEmptyStringIsPassed() = runBlocking {
        Assert.assertTrue(
            viewModel.validateInput("") == CreateNewSubjectInputValidation.INPUT_FIELD_IS_BLANK
        )
    }

    @Test
    fun validateInput_ShouldReturnFIELD_CONTAINS_INVALID_CHARS_IfSubjectNameContainsInvalidChars() =
        runBlocking {
        Assert.assertTrue(
            viewModel.validateInput("/") == CreateNewSubjectInputValidation.INPUT_FIELD_CONTAINS_INVALID_CHARS
        )
    }

    @Test
    fun validateInput_ShouldReturnSUBJECT_ALREADY_EXISTS_IfSubjectAlreadyExists() = runBlocking {
        whenever(mockSubjectRepository.getSubjectByName(testSubjectName)).thenReturn(
            Subject(
                testSubjectName,
                ""
            )
        )

        Assert.assertTrue(
            viewModel.validateInput(testSubjectName) == CreateNewSubjectInputValidation.SUBJECT_ALREADY_EXISTS
        )
    }

    @Test
    fun validateInput_ShouldReturnCORRECT_IfEverythingIsCorrect() = runBlocking {
        whenever(mockSubjectRepository.getSubjectByName(testSubjectName)).thenReturn(null)

        Assert.assertTrue(
            viewModel.validateInput(testSubjectName) == CreateNewSubjectInputValidation.CORRECT
        )
    }
}