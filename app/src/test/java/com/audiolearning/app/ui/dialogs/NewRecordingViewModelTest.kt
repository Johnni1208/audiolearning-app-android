package com.audiolearning.app.ui.dialogs

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.repositories.AudioRepository
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.ui.dialogs.new_recording.NewRecordingDialog
import com.audiolearning.app.ui.dialogs.new_recording.NewRecordingDialogViewModel
import com.audiolearning.app.ui.dialogs.new_recording.NewRecordingInputValidation
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

class NewRecordingViewModelTest {
    private lateinit var viewModel: NewRecordingDialogViewModel
    private lateinit var mockSubjectRepository: SubjectRepository
    private lateinit var mockAudioRepository: AudioRepository

    @Before
    fun setUp() {
        mockSubjectRepository = mock()
        mockAudioRepository = mock()

        viewModel = NewRecordingDialogViewModel(mockSubjectRepository, mockAudioRepository)
    }

    @Test
    fun getSubjects_ShouldLoadSubjectsFromRepository() {
        viewModel.getSubjects()

        verify(mockSubjectRepository).getAllSubjects()
    }

    @Test
    fun saveAudio_ShouldSaveAudioViaTheRepository() = runBlocking {
        viewModel.saveAudio(File(""), "", Subject("", ""))

        verify(mockAudioRepository).insert(File(""), "", Subject("", ""))
    }

    @Test
    fun validateInput_ShouldReturnNAME_IS_BLANK_IfNameIsEmpty() {
        assertTrue(
            viewModel.validateInput(
                "",
                Subject("", "")
            ) == NewRecordingInputValidation.NAME_IS_BLANK
        )
    }

    @Test
    fun validateInput_ShouldReturnNAME_CONTAINS_INVALID_CHARS_IfNameContainsInvalidChars() {
        val invalidChars = "\\|?*<\":>/'"

        for (char in invalidChars) {
            assertTrue(
                viewModel.validateInput(
                    char.toString(),
                    Subject("", "")
                ) == NewRecordingInputValidation.NAME_CONTAINS_INVALID_CHARS
            )
        }
    }

    @Test
    fun validateInput_ShouldReturnSUBJECT_IS_BLANK_IfSubjectIsNoRealSubject() {
        assertTrue(
            viewModel.validateInput(
                "test",
                Subject("", "").apply { isRealSubject = false }
            ) == NewRecordingInputValidation.SUBJECT_IS_BLANK)
    }

    @Test
    fun validateInput_ShouldReturnCORRECT_IfEverythingIsCorrect() {
        assertTrue(
            viewModel.validateInput(
                "test",
                Subject("", "").apply {
                    isRealSubject = true
                }) == NewRecordingInputValidation.CORRECT
        )
    }

    @Test
    fun getAddHintItemSelectListener_ShouldReturnAListenerWhichOpensADialog_WhenFirstItemIsSelected() {
        val mockFragmentManager: FragmentManager = mock()
        val mockFragmentTransaction: FragmentTransaction = mock()
        whenever(mockFragmentManager.beginTransaction()).thenReturn(mockFragmentTransaction)

        viewModel.getAddHintItemSelectedListener(mockFragmentManager)
            .onItemSelected(null, null, 0, 0)

        // Showing a dialog internally calls beginTransaction on the fragment manager
        verify(mockFragmentManager).beginTransaction()
    }

    @Test
    fun receiveNewRecordingFromArguments_ShouldReturnFileWithPathFromArgument() {
        val path = "\\testPath"
        val args: Bundle = mock()
        whenever(args.getString(NewRecordingDialog.ARG_NEW_FILE_PATH)).thenReturn(path)

        val file = viewModel.receiveNewRecordingFromArguments(args)

        assertEquals(path, file.path)
    }
}