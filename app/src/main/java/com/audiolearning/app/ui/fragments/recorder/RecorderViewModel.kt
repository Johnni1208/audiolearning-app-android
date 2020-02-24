package com.audiolearning.app.ui.fragments.recorder

import androidx.lifecycle.ViewModel
import com.audiolearning.app.audio.audio_recorder.IAudioRecorder
import com.audiolearning.app.util.timer.ITimer

/**
 * ViewModel for the RecorderFragment.
 *
 * @param audioRecorder Inject a custom instance of [IAudioRecorder].
 *
 * @param timer Inject a custom instance of [ITimer].
 */
class RecorderViewModel(
    private val audioRecorder: IAudioRecorder,
    timer: ITimer
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