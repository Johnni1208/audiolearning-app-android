package com.audiolearning.app.ui.fragment

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.repositories.AudioRepository
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.data.store.SelectedEntityStore
import com.audiolearning.app.service.audioplayer.AudioPlayerServiceConnection
import com.audiolearning.app.ui.fragment.audiosofsubject.AudiosOfSubjectFragmentViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AudiosOfSubjectFragmentViewModelTest {
    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AudiosOfSubjectFragmentViewModel
    private lateinit var selectedRecordingStore: SelectedEntityStore<Audio>
    private val mockSubjectsRepository: SubjectRepository = mock()
    private val mockAudioRepository: AudioRepository = mock()
    private val mockAudioPlayerServiceConnection: AudioPlayerServiceConnection = mock()
    private val testSubjectId = 1
    private val testSubject = Subject("testSubject", "").apply { id = 1 }
    private val testAudio = Audio("testAudio", "", 0, testSubjectId).apply { id = 1 }
    private val testPlayingAudioMetadata = MediaMetadataCompat.Builder()
        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "${testAudio.id!!}").build()

    @Before
    fun setup() {
        selectedRecordingStore = SelectedEntityStore()
        viewModel =
            AudiosOfSubjectFragmentViewModel(
                mockSubjectsRepository,
                mockAudioRepository,
                selectedRecordingStore,
                mockAudioPlayerServiceConnection
            )

        whenever(mockAudioPlayerServiceConnection.nowPlaying).thenReturn(
            MutableLiveData(
                testPlayingAudioMetadata
            )
        )

        whenever(mockAudioPlayerServiceConnection.transportControls).thenReturn(
            Mockito.mock(MediaControllerCompat.TransportControls::class.java)
        )
    }

    @Test
    fun getAudios_ShouldCallRepository() {
        runBlocking {
            whenever(mockSubjectsRepository.getSubjectById(testSubjectId)).thenReturn(testSubject)
            viewModel.setSubject(testSubjectId)
        }

        viewModel.audios

        verify(mockAudioRepository).getAudiosOfSubjectLiveData(testSubjectId)
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

        verify(mockAudioRepository).getAudiosOfSubjectLiveData(testSubjectId)
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

        assertTrue(viewModel.selectedAudiosList.value?.isEmpty()!!)
    }

    @Test
    fun deleteAllSelectedAudios_ShouldStopPlayingAudio() = runBlocking {
        viewModel.selectAudio(testAudio)

        viewModel.deleteAllSelectedAudios()

        verify(mockAudioPlayerServiceConnection.transportControls).stop()
    }
}
