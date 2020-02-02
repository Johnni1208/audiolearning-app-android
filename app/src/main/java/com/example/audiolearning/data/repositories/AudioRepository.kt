package com.example.audiolearning.data.repositories

import android.net.Uri
import com.example.audiolearning.data.db.AudioLearningDatabase
import com.example.audiolearning.data.db.entities.Audio
import com.example.audiolearning.data.db.entities.Subject
import com.example.audiolearning.util.file.AudioFileUtils
import java.io.File

class AudioRepository(
    private val db: AudioLearningDatabase
) {
    suspend fun insert(tempFile: File, name: String, subject: Subject) {
        val subjectDirectory = Uri.parse(subject.directoryUriString).path!!

        AudioFileUtils.cutFileAndPasteToDirectory(
            tempFile,
            subjectDirectory,
            name
        )

        val audioFile =
            File(subjectDirectory + File.separatorChar + name + Audio.fileExtension)
        val audioFileUri = Uri.fromFile(audioFile)


        db.getAudioDao().insert(Audio(name, subject.id!!, audioFileUri.toString()))
    }
}