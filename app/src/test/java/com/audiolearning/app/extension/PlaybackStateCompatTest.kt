package com.audiolearning.app.extension

import android.support.v4.media.session.PlaybackStateCompat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PlaybackStateCompatTest {
    @Test
    fun isPrepared_ShouldReturnCorrectBoolean() {
        var state =
            PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PLAYING, 0, 0F).build()
        assertTrue(state.isPrepared)
        state =
            PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PAUSED, 0, 0F).build()
        assertTrue(state.isPrepared)
        state = PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_BUFFERING, 0, 0F)
            .build()
        assertTrue(state.isPrepared)

        state =
            PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_STOPPED, 0, 0F).build()
        assertFalse(state.isPrepared)
    }

    @Test
    fun isPlaying_ShouldReturnCorrectBoolean() {
        var state =
            PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PLAYING, 0, 0F).build()
        assertTrue(state.isPlaying)
        state = PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_BUFFERING, 0, 0F)
            .build()
        assertTrue(state.isPlaying)

        state =
            PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_STOPPED, 0, 0F).build()
        assertFalse(state.isPlaying)
    }

    @Test
    fun isPausing_ShouldReturnCorrectBoolean() {
        var state =
            PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PAUSED, 0, 0F).build()
        assertTrue(state.isPausing)

        state =
            PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PLAYING, 0, 0F).build()
        assertFalse(state.isPausing)
    }

    @Test
    fun currentPlaybackPosition_ShouldReturnCorrectPosition() {
        val state =
            PlaybackStateCompat.Builder().setState(PlaybackStateCompat.STATE_PLAYING, 420, 1F)
                .build()

        assertEquals(420, state.currentPlaybackPosition)
    }
}
