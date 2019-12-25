package com.example.audiolearning.fragments.recorder

import android.annotation.TargetApi
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.audiolearning.audio.audio_recorder.AudioRecorder
import com.example.audiolearning.audio.audio_recorder.AudioRecorderState
import com.example.audiolearning.audio.audio_recorder.IAudioRecorder
import com.example.audiolearning.util.timer.ITimer
import com.example.audiolearning.util.timer.Timer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

/**
 * ViewModel for the RecorderFragment.
 *
 * @param audioRecorder Inject a custom instance of [IAudioRecorder],
 * else it uses a normal [AudioRecorder].
 */
class RecorderViewModel(private val audioRecorder: IAudioRecorder = AudioRecorder()) : ViewModel() {

    /* AudioRecorder State */
    private val _audioRecorderState = MutableLiveData<AudioRecorderState>().apply {
        value = AudioRecorderState.IDLING
    }
    val audioRecorderState: LiveData<AudioRecorderState>
        get() = _audioRecorderState

    /* Recorded file output */
    private val _recordedFile = MutableLiveData<File>().apply {
        value = null
    }
    val recordedFile: LiveData<File>
        get() = _recordedFile

    private val recordTimer: ITimer =
        Timer()
    val recordedTime: LiveData<String>
        get() = recordTimer.time

    fun onRecordOrStop() {
        val isRecording = _audioRecorderState.value == AudioRecorderState.RECORDING

        if (isRecording) {
            _audioRecorderState.value = AudioRecorderState.IDLING
            stopRecording()
        } else {
            _audioRecorderState.value = AudioRecorderState.RECORDING
            startRecording()
        }
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
        val isPausing = _audioRecorderState.value == AudioRecorderState.PAUSING

        if (isPausing) {
            _audioRecorderState.value = AudioRecorderState.RECORDING
            resumeRecording()
        } else {
            _audioRecorderState.value = AudioRecorderState.PAUSING
            pauseRecording()
        }
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
        }
    }
}