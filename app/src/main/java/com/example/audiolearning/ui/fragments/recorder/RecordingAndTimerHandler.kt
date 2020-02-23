package com.example.audiolearning.ui.fragments.recorder

import android.annotation.TargetApi
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.audiolearning.audio.audio_recorder.AudioRecorderState
import com.example.audiolearning.audio.audio_recorder.IAudioRecorder
import com.example.audiolearning.util.timer.ITimer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class RecordingAndTimerHandler(
    private val audioRecorder: IAudioRecorder,
    timer: ITimer
) {
    private val _audioRecorderState = MutableLiveData<AudioRecorderState>().apply {
        value = AudioRecorderState.IDLING
    }
    val audioRecorderState: LiveData<AudioRecorderState>
        get() = _audioRecorderState

    private val _recordedFile = MutableLiveData<File>().apply {
        value = null
    }
    val recordedFile: LiveData<File>
        get() = _recordedFile

    private val recordTimer: ITimer =
        timer
    val recordedTime: LiveData<String>
        get() = recordTimer.time

    private val isRecording: Boolean
        get() = _audioRecorderState.value == AudioRecorderState.RECORDING
    private val isPausing: Boolean
        get() = _audioRecorderState.value == AudioRecorderState.PAUSING

    fun onRecordOrStop() {
        if (!isRecording && !isPausing) {
            startRecording()
            _audioRecorderState.value = AudioRecorderState.RECORDING
            return
        }

        stopRecording()
        _audioRecorderState.value = AudioRecorderState.IDLING
    }

    private fun stopRecording() {
        recordTimer.stop()
        GlobalScope.launch {
            val file = audioRecorder.stop()
            _recordedFile.postValue(file)
        }
    }

    private fun startRecording() {
        recordTimer.start()
        audioRecorder.record()
    }

    fun onPauseOrResume() {
        if (isPausing) {
            resumeRecording()
            _audioRecorderState.value = AudioRecorderState.RECORDING
            return
        }

        pauseRecording()
        _audioRecorderState.value = AudioRecorderState.PAUSING
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun pauseRecording() {
        recordTimer.pause()
        audioRecorder.pause()
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun resumeRecording() {
        recordTimer.resume()
        audioRecorder.resume()
    }

    fun onDestroy() {
        if (audioRecorder.isActive) {
            audioRecorder.onDestroy()
            _audioRecorderState.value = AudioRecorderState.IDLING
            recordTimer.stop()
        }
    }
}