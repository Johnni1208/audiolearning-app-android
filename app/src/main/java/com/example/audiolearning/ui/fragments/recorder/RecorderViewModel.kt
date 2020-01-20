package com.example.audiolearning.ui.fragments.recorder

import androidx.lifecycle.ViewModel
import com.example.audiolearning.audio.audio_recorder.IAudioRecorder
import com.example.audiolearning.data.repositories.AudioRepository
import com.example.audiolearning.util.timer.ITimer

/**
 * ViewModel for the RecorderFragment.
 *
 * @param audioRecorder Inject a custom instance of [IAudioRecorder].
 *
 * @param timer Inject a custom instance of [ITimer].
 *
 * @param audioRepository The database access for audio files.
 */
class RecorderViewModel(
    private val audioRecorder: IAudioRecorder,
    timer: ITimer,
    private val audioRepository: AudioRepository
) : ViewModel() {

    val recordingAndTimerHandler =
        RecordingAndTimerHandler(
            audioRecorder,
            timer
        )

    fun onDestroy() {
        recordingAndTimerHandler.onDestroy()
    }
}