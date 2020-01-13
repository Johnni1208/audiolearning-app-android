package com.example.audiolearning.ui.fragments.recorder

import android.annotation.TargetApi
import android.net.Uri
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.audiolearning.audio.audio_recorder.AudioRecorder
import com.example.audiolearning.audio.audio_recorder.AudioRecorderState
import com.example.audiolearning.audio.audio_recorder.IAudioRecorder
import com.example.audiolearning.data.db.entities.Audio
import com.example.audiolearning.data.db.entities.Subject
import com.example.audiolearning.data.repositories.AudioRepository
import com.example.audiolearning.ui.dialogs.new_recording.NewRecordingDialogButtonsListener
import com.example.audiolearning.util.AudioFileUtils
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
 *
 * @param timer Inject a custom instance of [ITimer],
 * else it uses a normal [Timer]
 */
class RecorderViewModel(
    private val audioRecorder: IAudioRecorder,
    timer: ITimer,
    private val audioRepository: AudioRepository
) : ViewModel() {

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

    fun getNewRecordingDialogButtonsListener(newFile: File) =
        object : NewRecordingDialogButtonsListener {
            override fun onSaveButtonClicked(name: String, subject: Subject) {
                onSaveAudio(newFile, name, subject)
            }

            override fun onDiscardButtonClicked() {
                newFile.delete()
            }
        }

    fun onSaveAudio(file: File, name: String, subject: Subject) {
        AudioFileUtils.moveFileToDirectory(
            file,
            subject.directory.absolutePath,
            name
        )

        val audioFile =
            File(subject.directory.absolutePath + "/" + name + AudioFileUtils.fileExtension)
        val audioFileUri = Uri.fromFile(audioFile)

        GlobalScope.launch {
            audioRepository.upsert(Audio(name, subject.id!!, audioFileUri.toString()))
        }
    }

    fun onDestroy() {
        if (audioRecorder.isActive) {
            audioRecorder.onDestroy()
            _audioRecorderState.value = AudioRecorderState.IDLING
        }
    }
}