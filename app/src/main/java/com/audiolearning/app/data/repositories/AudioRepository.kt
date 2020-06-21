package com.audiolearning.app.data.repositories

import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.core.net.toUri
import com.audiolearning.app.data.db.AudioLearningDatabase
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.util.file.AudioFileUtils
import java.io.File
import javax.inject.Inject

class AudioRepository @Inject constructor(private val db: AudioLearningDatabase) {
    suspend fun insert(tempAudio: File, name: String, subject: Subject) {
        val subjectDirectory = Uri.parse(subject.directoryUriString).path!!

        AudioFileUtils.cutFileAndPasteToDirectory(
            tempAudio,
            subjectDirectory,
            name
        )

        val audioFile =
            File(subjectDirectory + File.separatorChar + name + Audio.fileExtension)
        val audioFileUri: Uri = audioFile.toUri()

        val audioDuration: Long = MediaMetadataRetriever().run {
            setDataSource(audioFile.absolutePath)
            val time = extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()
            release()
            return@run time
        }

        db.getAudioDao().insert(Audio(name, audioFileUri.toString(), audioDuration, subject.id!!))
    }

    suspend fun delete(audio: Audio) {
        AudioFileUtils.deleteAudioFile(audio)
        db.getAudioDao().delete(audio)
    }

    fun getAudiosOfSubject(subjectId: Int) = db.getAudioDao().getAudiosOfSubject(subjectId)

    suspend fun getAudioById(id: Int) = db.getAudioDao().getAudioById(id)
}