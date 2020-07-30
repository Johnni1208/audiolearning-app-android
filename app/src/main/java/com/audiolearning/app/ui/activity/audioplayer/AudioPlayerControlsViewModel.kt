package com.audiolearning.app.ui.activity.audioplayer

import android.os.Bundle
import androidx.core.net.toUri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.data.repositories.AudioRepository
import com.audiolearning.app.data.repositories.SubjectRepository
import com.audiolearning.app.extension.currentPlaybackPosition
import com.audiolearning.app.extension.duration
import com.audiolearning.app.extension.from
import com.audiolearning.app.extension.id
import com.audiolearning.app.extension.isPausing
import com.audiolearning.app.extension.isPlayEnabled
import com.audiolearning.app.extension.isPlaying
import com.audiolearning.app.extension.isPrepared
import com.audiolearning.app.service.audioplayer.AudioPlayerServiceConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class AudioPlayerControlsViewModel @ViewModelInject constructor(
    private val audioRepository: AudioRepository,
    private val subjectRepository: SubjectRepository,
    private val audioPlayerServiceConnection: AudioPlayerServiceConnection
) : ViewModel() {
    suspend fun playAudio(audio: Audio) {
        val nowPlaying = audioPlayerServiceConnection.nowPlaying.value
        val transportControls = audioPlayerServiceConnection.transportControls

        val isPrepared = audioPlayerServiceConnection.playBackState.value?.isPrepared ?: false
        if (isPrepared && audio.id!! == nowPlaying?.id?.toInt()) {
            audioPlayerServiceConnection.playBackState.value?.let { playbackState ->
                when {
                    playbackState.isPausing -> transportControls.pause()
                    playbackState.isPlayEnabled -> transportControls.play()
                    else -> {
                        Timber.w(
                            """Playable item clicked but neither play nor pause are enabled! (mediaId=${audio.id})"""
                        )
                    }
                }
            }
        } else {
            val subject = getSubjectById(audio.subjectId)

            transportControls.playFromUri(
                audio.fileUriString.toUri(),
                Bundle().from(audio, subject)
            )
        }
    }

    suspend fun playAudioId(audioId: String) {
        val nowPlaying = audioPlayerServiceConnection.nowPlaying.value
        val transportControls = audioPlayerServiceConnection.transportControls

        val isPrepared = audioPlayerServiceConnection.playBackState.value?.isPrepared ?: false
        if (isPrepared && audioId == nowPlaying?.id) {
            audioPlayerServiceConnection.playBackState.value?.let { playbackState ->
                when {
                    playbackState.isPlaying -> transportControls.pause()
                    playbackState.isPlayEnabled -> transportControls.play()
                    else -> {
                        Timber.w(
                            "Playable item clicked but neither play nor pause are enabled! (mediaId=$audioId)"
                        )
                    }
                }
            }
        } else {
            val audio = getAudioById(audioId.toInt())
            val subject = getSubjectById(audio.subjectId)

            transportControls.playFromUri(
                audio.fileUriString.toUri(),
                Bundle().from(audio, subject)
            )
        }
    }

    fun stop() {
        audioPlayerServiceConnection.transportControls.stop()
    }

    fun fastForward() {
        var seekTime = audioPlayerServiceConnection.playBackState.value
            ?.currentPlaybackPosition?.plus(SEEK_TIME) ?: 0

        val audioDuration = audioPlayerServiceConnection.nowPlaying.value?.duration ?: 0

        if (seekTime > audioDuration) {
            seekTime = audioDuration - 1000
        }

        audioPlayerServiceConnection.transportControls.seekTo(seekTime)
    }

    fun rewind() {
        var seekTime = audioPlayerServiceConnection.playBackState.value
            ?.currentPlaybackPosition?.minus(SEEK_TIME) ?: 0

        if (seekTime < 0) seekTime = 0

        audioPlayerServiceConnection.transportControls.seekTo(seekTime)
    }

    fun seekTo(seekTime: Long) = audioPlayerServiceConnection.transportControls.seekTo(seekTime)


    private suspend fun getAudioById(id: Int) = withContext(Dispatchers.IO) {
        return@withContext audioRepository.getAudioById(id)
            ?: throw IllegalArgumentException("No audio with id: $id")
    }

    private suspend fun getSubjectById(id: Int) = withContext(Dispatchers.IO) {
        return@withContext subjectRepository.getSubjectById(id)
            ?: throw IllegalArgumentException("No subject with id: $id")
    }
}

private const val SEEK_TIME = 10000
