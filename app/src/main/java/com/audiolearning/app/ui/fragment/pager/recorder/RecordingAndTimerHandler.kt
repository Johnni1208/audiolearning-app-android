package com.audiolearning.app.ui.fragment.pager.recorder

import android.annotation.TargetApi
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.audiolearning.app.audio.recorder.AudioRecorder
import com.audiolearning.app.audio.recorder.AudioRecorderState
import com.audiolearning.app.timer.Timer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class RecordingAndTimerHandler(
    private val audioRecorder: AudioRecorder,
    timer: Timer
) {
    private val _audioRecorderState = MutableLiveData<AudioRecorderState>().apply {
        value = audioRecorder.state
    }
    val audioRecorderState: LiveData<AudioRecorderState>
        get() = _audioRecorderState

    val audioRecorderMaxAmplitude: Int
        get() = audioRecorder.maxAmplitude

    private val _recordedFile = MutableLiveData<File>().apply {
        value = null
    }
    val recordedFile: LiveData<File>
        get() = _recordedFile

    private val recordTimer: Timer =
        timer
    val recordedTime: LiveData<String>
        get() = recordTimer.time

    fun onRecordOrStop() {
        if (audioRecorder.state == AudioRecorderState.IDLING) startRecording()
        else stopRecording()
    }

    private fun startRecording() {
        recordTimer.start()
        audioRecorder.record()
        _audioRecorderState.postValue(audioRecorder.state)
    }

    private fun stopRecording() {
        recordTimer.stop()
        // We have to do it earlier to prevent bugs audioRecorder.stop() changes the state to Idling, too.
        _audioRecorderState.postValue(AudioRecorderState.IDLING)

        CoroutineScope(Dispatchers.Unconfined).launch {
            val file = audioRecorder.stop()
            _recordedFile.postValue(file)
        }
    }

    fun onPauseOrResume() {
        if (audioRecorder.state == AudioRecorderState.PAUSING) resumeRecording()
        else pauseRecording()
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun resumeRecording() {
        recordTimer.resume()
        audioRecorder.resume()
        _audioRecorderState.postValue(audioRecorder.state)
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun pauseRecording() {
        recordTimer.pause()
        audioRecorder.pause()
        _audioRecorderState.postValue(audioRecorder.state)
    }

    fun onDestroy() {
        if (audioRecorder.state != AudioRecorderState.IDLING) {
            audioRecorder.onDestroy()
            _audioRecorderState.postValue(audioRecorder.state)
            recordTimer.stop()
        }
    }
}
