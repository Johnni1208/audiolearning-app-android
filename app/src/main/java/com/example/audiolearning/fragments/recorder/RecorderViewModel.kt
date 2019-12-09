package com.example.audiolearning.fragments.recorder

import android.annotation.TargetApi
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.audiolearning.audio.audio_recorder.AudioRecorder
import com.example.audiolearning.audio.audio_recorder.AudioRecorderState
import com.example.audiolearning.audio.audio_recorder.IAudioRecorder
import kotlinx.coroutines.runBlocking
import java.io.File

class RecorderViewModel : ViewModel() {

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

    private var isRecording = false
    private var isPausing = false
    private val audioRecorder: IAudioRecorder = AudioRecorder()

    fun onRecordOrStop() {
        if (isRecording) {
            _audioRecorderState.value = AudioRecorderState.IDLING
            isPausing = false

            stopRecording()
        } else {
            _audioRecorderState.value = AudioRecorderState.RECORDING
            startRecording()
        }

        isRecording = !isRecording
    }

    private fun stopRecording() {
        runBlocking {
            _recordedFile.value = audioRecorder.stop()
        }
    }

    private fun startRecording() {
        audioRecorder.record()
    }

    fun onPauseOrResume() {
        if (isPausing) {
            _audioRecorderState.value = AudioRecorderState.RESUMING
            resumeRecording()
        } else {
            _audioRecorderState.value = AudioRecorderState.PAUSING
            pauseRecording()
        }

        isPausing = !isPausing
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun pauseRecording() {
        audioRecorder.pause()
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun resumeRecording() {
        audioRecorder.resume()
    }
}