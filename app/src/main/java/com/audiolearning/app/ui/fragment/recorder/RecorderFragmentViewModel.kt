package com.audiolearning.app.ui.fragment.recorder

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.audiolearning.app.audio.recorder.AudioRecorder
import com.audiolearning.app.service.audioplayer.AudioPlayerServiceConnection
import com.audiolearning.app.timer.Timer

/**
 * ViewModel for the RecorderFragment.
 *
 * @param audioRecorder Inject a custom instance of [AudioRecorder].
 *
 * @param timer Inject a custom instance of [Timer].
 */
class RecorderFragmentViewModel @ViewModelInject constructor(
    private val audioRecorder: AudioRecorder,
    timer: Timer,
    // Must be init here, since else it is in an uninitialized state later
    private val audioPlayerServiceConnection: AudioPlayerServiceConnection
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
