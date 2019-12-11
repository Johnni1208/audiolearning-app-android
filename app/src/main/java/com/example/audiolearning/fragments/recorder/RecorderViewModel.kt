package com.example.audiolearning.fragments.recorder

import android.annotation.TargetApi
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.audiolearning.audio.audio_recorder.AudioRecorder
import com.example.audiolearning.audio.audio_recorder.IAudioRecorder
import com.example.audiolearning.util.timer.Timer
import kotlinx.coroutines.runBlocking
import java.io.File

class RecorderViewModel : ViewModel() {

    private val _recordAndStopButtonText = MutableLiveData<String>().apply {
        value = "Record"
    }
    val recordAndStopButtonText: LiveData<String>
        get() = _recordAndStopButtonText

    private val _pauseAndResumeButtonText = MutableLiveData<String>().apply {
        value = "Pause"
    }
    val pauseAndResumeButtonText: LiveData<String>
        get() = _pauseAndResumeButtonText

    private val _recordedFile = MutableLiveData<File>().apply {
        value = null
    }
    val recordedFile: LiveData<File>
        get() = _recordedFile

    private val recordTimer: Timer =
        Timer()
    val recordedTime: LiveData<String>
        get() = recordTimer.time

    private var isRecording = false
    private var isPausing = false
    private val audioRecorder: IAudioRecorder = AudioRecorder()


    fun onRecordOrStop() {
        if (isRecording) {
            stopRecording()
        } else {
            startRecording()
        }

        isRecording = !isRecording
    }

    private fun stopRecording() {
        recordTimer.stop()
        _recordAndStopButtonText.value = "Record"
        runBlocking {
            _recordedFile.value = audioRecorder.stop()
        }
    }

    private fun startRecording() {
        audioRecorder.record()
        recordTimer.start()
        _recordAndStopButtonText.value = "Stop"
    }

    fun onPauseOrResume() {
        if (isPausing) {
            resumeRecording()
        } else {
            pauseRecording()
        }

        isPausing = !isPausing
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun pauseRecording() {
        audioRecorder.pause()
        recordTimer.pause()
        _pauseAndResumeButtonText.value = "Resume"
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun resumeRecording() {
        audioRecorder.resume()
        recordTimer.resume()
        _pauseAndResumeButtonText.value = "Pause"
    }
}