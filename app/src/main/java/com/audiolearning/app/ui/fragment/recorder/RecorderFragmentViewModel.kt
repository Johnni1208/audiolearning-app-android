package com.audiolearning.app.ui.fragment.recorder

import androidx.lifecycle.ViewModel
import com.audiolearning.app.audio.recorder.AudioRecorder
import com.audiolearning.app.util.timer.Timer
import javax.inject.Inject

/**
 * ViewModel for the RecorderFragment.
 *
 * @param audioRecorder Inject a custom instance of [AudioRecorder].
 *
 * @param timer Inject a custom instance of [Timer].
 */
class RecorderFragmentViewModel @Inject constructor(
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
