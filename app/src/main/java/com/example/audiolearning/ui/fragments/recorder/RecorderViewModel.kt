package com.example.audiolearning.ui.fragments.recorder

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.audiolearning.audio.audio_recorder.AudioRecorder
import com.example.audiolearning.audio.audio_recorder.IAudioRecorder
import com.example.audiolearning.data.db.entities.Audio
import com.example.audiolearning.data.db.entities.Subject
import com.example.audiolearning.data.repositories.AudioRepository
import com.example.audiolearning.handler.RecordingAndTimerHandler
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

    val recordingAndTimerHandler =
        RecordingAndTimerHandler(
            audioRecorder,
            timer
        )

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
        recordingAndTimerHandler.onDestroy()
    }
}