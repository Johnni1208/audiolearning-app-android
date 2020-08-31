package com.audiolearning.app.ui.fragment.pager.recorder

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.audiolearning.app.audio.recorder.AudioRecorder
import com.audiolearning.app.timer.Timer

/**
 * ViewModel for the RecorderFragment.
 *
 * @param audioRecorder Inject a custom instance of [AudioRecorder].
 *
 * @param timer Inject a custom instance of [Timer].
 */
class RecorderPagerFragmentViewModel @ViewModelInject constructor(
    private val audioRecorder: AudioRecorder,
    timer: Timer
) : ViewModel() {
    val recordingAndTimerHandler =
        RecordingAndTimerHandler(
            audioRecorder,
            timer
        )
}
