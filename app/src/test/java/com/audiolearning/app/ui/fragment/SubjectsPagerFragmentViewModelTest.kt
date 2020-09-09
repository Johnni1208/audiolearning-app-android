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
import com.audiolearning.app.ui.fragment.pager.subjects.SubjectsPagerFragmentViewModel
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SubjectsPagerFragmentViewModelTest {
    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testSubject = Subject("testSubject", "testDir").apply { id = 1 }
    private val testAudio = Audio("test", "test", 0, 1, 0).apply { id = 1 }
    private val testPlayingAudioMetadata = MediaMetadataCompat.Builder()
        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "${testAudio.id!!}").build()
    private val mockSubjectRepository: SubjectRepository = mock()
    private val mockAudioRepository: AudioRepository = mock()
    private var mockAudioPlayerServiceConnection: AudioPlayerServiceConnection = mock()
    private lateinit var selectedEntityStore: SelectedEntityStore<Subject>
    private lateinit var pagerFragmentViewModel: SubjectsPagerFragmentViewModel

    @Before
    fun setup() {
        selectedEntityStore = SelectedEntityStore()
        pagerFragmentViewModel = SubjectsPagerFragmentViewModel(
            mockSubjectRepository,
            mockAudioRepository,
            selectedEntityStore,
            mockAudioPlayerServiceConnection
        )

        whenever(mockAudioPlayerServiceConnection.nowPlaying).thenReturn(
            MutableLiveData(
                testPlayingAudioMetadata
            )
        )

        whenever(mockAudioPlayerServiceConnection.transportControls).thenReturn(
            mock(MediaControllerCompat.TransportControls::class.java)
        )

        runBlocking {
            whenever(mockAudioRepository.getAudiosOfSubject(testSubject.id!!)).thenReturn(
                listOf(testAudio)
            )
        }
    }

    @Test
    fun getSubjects_ShouldCallRepository() {
        pagerFragmentViewModel.subjects

        verify(mockSubjectRepository).getAllSubjects()
    }

    @Test
    fun deleteAllSelectedSubjects_ShouldClearSelectedSubjectsList() = runBlocking {
        pagerFragmentViewModel.selectSubject(testSubject)

        pagerFragmentViewModel.deleteAllSelectedSubjects()

        assertTrue(pagerFragmentViewModel.selectedSubjectsList.value?.isEmpty()!!)
    }

    @Test
    fun deleteAllSelectedSubjects_ShouldDeleteSelectedSubject() = runBlocking {
        pagerFragmentViewModel.selectSubject(testSubject)

        pagerFragmentViewModel.deleteAllSelectedSubjects()

        verify(mockSubjectRepository).delete(testSubject)
    }

    @Test
    fun deleteAllSelectedSubjects_ShouldStopPlayingAudio() = runBlocking {
        pagerFragmentViewModel.selectSubject(testSubject)

        pagerFragmentViewModel.deleteAllSelectedSubjects()

        verify(mockAudioPlayerServiceConnection.transportControls).stop()
    }
}
