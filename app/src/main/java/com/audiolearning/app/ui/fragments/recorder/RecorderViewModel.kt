package com.audiolearning.app.ui.fragments.recorder

import androidx.lifecycle.ViewModel
import com.audiolearning.app.audio.audio_recorder.AudioRecorder
import com.audiolearning.app.audio.audio_recorder.IAudioRecorder
import com.audiolearning.app.util.timer.ITimer
import com.audiolearning.app.util.timer.Timer
import javax.inject.Inject

/**
 * ViewModel for the RecorderFragment.
 *
 * @param audioRecorder Inject a custom instance of [IAudioRecorder].
 *
 * @param timer Inject a custom instance of [ITimer].
 */
class RecorderViewModel @Inject constructor(
    private val audioRecorder: AudioRecorder,
    timer: Timer
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