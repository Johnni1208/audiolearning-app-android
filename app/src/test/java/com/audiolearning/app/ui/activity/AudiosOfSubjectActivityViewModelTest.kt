package com.audiolearning.app.ui.activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.repositories.AudioRepository
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.data.store.SelectedEntityStore
import com.audiolearning.app.ui.activity.audiosofsubject.AudiosOfSubjectActivityViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AudiosOfSubjectActivityViewModelTest {
    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AudiosOfSubjectActivityViewModel
    private lateinit var selectedRecordingStore: SelectedEntityStore<Audio>
    private val mockSubjectsRepository: SubjectRepository = mock()
    private val mockAudioRepository: AudioRepository = mock()
    private val testSubjectId = 1
    private val testSubject = Subject("testSubject", "").apply { id = 1 }
    private val testAudio = Audio("testAudio", "", 0, testSubjectId)

    @Before
    fun setup() {
        selectedRecordingStore = SelectedEntityStore()
        viewModel = AudiosOfSubjectActivityViewModel(
            mockSubjectsRepository,
            mockAudioRepository,
            selectedRecordingStore
        )
    }

    @Test
    fun setSubject_ShouldSetCorrectSubject() = runBlocking {
        whenever(mockSubjectsRepository.getSubjectById(testSubjectId)).thenReturn(testSubject)

        viewModel.setSubject(testSubjectId)

        assertEquals(testSubject, viewModel.subject.value)
    }

    @Test
    fun getAudios_ShouldCallAudioRepository() {
        runBlocking {
            whenever(mockSubjectsRepository.getSubjectById(testSubjectId)).thenReturn(testSubject)
            viewModel.setSubject(testSubjectId)
        }

        viewModel.audios

        verify(mockAudioRepository).getAudiosOfSubject(testSubjectId)
    }

    @Test
    fun deleteAllSelectedAudios_ShouldCallRepositoryDelete() = runBlocking {
        viewModel.selectAudio(testAudio)

        viewModel.deleteAllSelectedAudios()

        verify(mockAudioRepository).delete(testAudio)
    }

    @Test
    fun deleteAllSelectedAudios_ShouldClearSelectedSubjectsList() = runBlocking {
        viewModel.selectAudio(testAudio)

        viewModel.deleteAllSelectedAudios()

        Assert.assertTrue(viewModel.selectedAudiosList.value?.isEmpty()!!)
    }
}