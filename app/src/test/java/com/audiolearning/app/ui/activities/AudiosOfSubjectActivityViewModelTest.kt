package com.audiolearning.app.ui.activities

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.repositories.AudioRepository
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.data.store.SelectedEntityStore
import com.audiolearning.app.ui.activities.audios_of_subject.AudiosOfSubjectActivityViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AudiosOfSubjectActivityViewModelTest {
    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModelOfSubject: AudiosOfSubjectActivityViewModel
    private lateinit var selectedRecordingStore: SelectedEntityStore<Audio>
    private val mockSubjectsRepository: SubjectRepository = mock()
    private val mockAudioRepository: AudioRepository = mock()
    private val testSubject = Subject("testName", "")

    @Before
    fun setup() {
        selectedRecordingStore = SelectedEntityStore()
        viewModelOfSubject = AudiosOfSubjectActivityViewModel(
            mockSubjectsRepository,
            mockAudioRepository,
            selectedRecordingStore
        )
    }

    @Test
    fun setSubject_ShouldSetCorrectSubject() = runBlocking {
        whenever(mockSubjectsRepository.getSubjectById(1)).thenReturn(testSubject)

        viewModelOfSubject.setSubject(1)

        assertEquals(testSubject, viewModelOfSubject.subject.value)
    }
}