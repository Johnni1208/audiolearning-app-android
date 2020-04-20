package com.audiolearning.app.data.repositories

import android.media.MediaMetadataRetriever
import android.net.Uri
import com.audiolearning.app.data.db.AudioLearningDatabase
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.util.file.AudioFileUtils
import java.io.File
import javax.inject.Inject

class AudioRepository @Inject constructor(
    private val db: AudioLearningDatabase
) {
    suspend fun insert(tempAudio: File, name: String, subject: Subject) {
        val subjectDirectory = Uri.parse(subject.directoryUriString).path!!

        AudioFileUtils.cutFileAndPasteToDirectory(
            tempAudio,
            subjectDirectory,
            name
        )

        val audioFile =
            File(subjectDirectory + File.separatorChar + name + Audio.fileExtension)
        val audioFileUri: Uri = Uri.fromFile(audioFile)

        // TODO: Length is wrong on API 22
        val audioDuration: Int = MediaMetadataRetriever().run {
            setDataSource(audioFile.path)
            Integer.parseInt(extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))
        }

        db.getAudioDao().insert(Audio(name, audioFileUri.toString(), audioDuration, subject.id!!))
    }

    fun getAudiosOfSubject(subjectId: Int) = db.getAudioDao().getAudiosOfSubject(subjectId)
}