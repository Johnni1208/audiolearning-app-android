package com.audiolearning.app.ui.activity.audioplayer

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.audiolearning.app.R
import com.audiolearning.app.extension.currentPlayBackPosition
import com.audiolearning.app.extension.date
import com.audiolearning.app.extension.displaySubtitle
import com.audiolearning.app.extension.duration
import com.audiolearning.app.extension.id
import com.audiolearning.app.extension.isPlaying
import com.audiolearning.app.extension.title
import com.audiolearning.app.extension.toTimeString
import com.audiolearning.app.service.audioplayer.AudioPlayerServiceConnection
import com.audiolearning.app.service.audioplayer.EMPTY_PLAYBACK_STATE
import com.audiolearning.app.service.audioplayer.NOTHING_PLAYING
import dagger.hilt.android.qualifiers.ApplicationContext

class AudioPlayerDataViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    audioPlayerServiceConnection: AudioPlayerServiceConnection
) : AndroidViewModel(context as Application) {
    data class AudioMetaData(
        val id: String,
        val title: String?,
        val subtitle: String?,
        val duration: String,
        val date: String?
    )

    val mediaMetaData = MutableLiveData<AudioMetaData>()
    val mediaPosition = MutableLiveData<Long>().apply {
        postValue(0L)
    }
    val mediaButtonRes = MutableLiveData<Int>().apply {
        postValue(R.drawable.ic_play_arrow_24dp)
    }

    private var playbackState: PlaybackStateCompat = EMPTY_PLAYBACK_STATE

    private var updatePosition = true
    private val handler = Handler(Looper.getMainLooper())

    private val playbackStateObserver = Observer<PlaybackStateCompat> {
        playbackState = it ?: EMPTY_PLAYBACK_STATE
        val metadata = audioPlayerServiceConnection.nowPlaying.value ?: NOTHING_PLAYING
        updateState(playbackState, metadata)
    }

    private val mediaMetadataObserver = Observer<MediaMetadataCompat> {
        updateState(playbackState, it)
    }

    private val audioPlayerServiceConnection = audioPlayerServiceConnection.also {
        it.playBackState.observeForever(playbackStateObserver)
        it.nowPlaying.observeForever(mediaMetadataObserver)
        checkPlaybackPosition()
    }

    private fun updateState(
        playbackState: PlaybackStateCompat,
        mediaMetadata: MediaMetadataCompat
    ) {
        if (mediaMetadata.duration != 0L && mediaMetadata.id != null) {
            val audioMetaData = AudioMetaData(
                mediaMetadata.id!!,
                mediaMetadata.title?.trim(),
                mediaMetadata.displaySubtitle?.trim(),
                mediaMetadata.duration.toTimeString(),
                mediaMetadata.date?.trim()
            )

            this.mediaMetaData.postValue(audioMetaData)
        }

        mediaButtonRes.postValue(
            when (playbackState.isPlaying) {
                true -> R.drawable.ic_pause_24dp
                else -> R.drawable.ic_play_arrow_24dp
            }
        )
    }

    private fun checkPlaybackPosition(): Boolean = handler.postDelayed({
        val currPosition = playbackState.currentPlayBackPosition
        if (mediaPosition.value != currPosition) mediaPosition.postValue(currPosition)

        if (updatePosition) checkPlaybackPosition()
    }, POSITION_UPDATE_INTERVAL_MILLIS)

    override fun onCleared() {
        super.onCleared()

        audioPlayerServiceConnection.playBackState.removeObserver(playbackStateObserver)
        audioPlayerServiceConnection.nowPlaying.removeObserver(mediaMetadataObserver)

        updatePosition = false
    }
}

private const val POSITION_UPDATE_INTERVAL_MILLIS = 100L
