package com.audiolearning.app.service.audioplayer

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.net.toUri
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.extension.from
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AudioPlaybackPreparerTest {
    private lateinit var mockExoPlayer: ExoPlayer
    private lateinit var mockDataSourceFactory: DataSource.Factory
    private val subject = Subject("testSubject", "").apply { id = 0 }
    private val audio = Audio("testAudio", "", 69, subject.id!!).apply { id = 0 }

    private lateinit var audioPlaybackPreparer: AudioPlaybackPreparer

    @Before
    fun setup() {
        mockExoPlayer = mock()
        mockDataSourceFactory = mock()

        audioPlaybackPreparer = AudioPlaybackPreparer(mockExoPlayer, mockDataSourceFactory)
    }

    @Test
    fun getSupportedPrepareActions_ShouldReturnPrepareFromUri() {
        val actions = audioPlaybackPreparer.supportedPrepareActions

        assertEquals(
            PlaybackStateCompat.ACTION_PREPARE_FROM_URI or
                    PlaybackStateCompat.ACTION_PLAY_FROM_URI,
            actions
        )
    }

    @Test
    fun onPrepareFromUri_ShouldPrepareExoplayer() {
        audioPlaybackPreparer.onPrepareFromUri(
            audio.fileUriString.toUri(),
            true,
            Bundle().from(audio, subject)
        )

        verify(mockExoPlayer).prepare(ArgumentMatchers.any(ProgressiveMediaSource::class.java))
        verify(mockExoPlayer).playWhenReady = true
    }
}
