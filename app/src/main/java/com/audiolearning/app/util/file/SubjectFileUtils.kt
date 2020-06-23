package com.audiolearning.app.util.file

import android.net.Uri
import com.audiolearning.app.data.db.entities.Subject
import java.io.File

/**
 * Utilities class for SubjectFiles in this project.
 */
object SubjectFileUtils {
    /**
     * Creates a new subject directory under "/subjects/[directoryName]"
     *
     * @param filesDir root directory, use context.filesDir
     * @param directoryName name for the new subject directory
     */
    fun createNewSubjectDirectory(filesDir: File, directoryName: String): File {
        val rootDir = filesDir.path + File.separatorChar + "subjects"

        val subjectDir = File(rootDir + File.separatorChar + directoryName)
        subjectDir.mkdirs()

        return subjectDir
    }

    /**
     * Deletes the directory of a [Subject] from the device
     *
     * @param subject The subject of which the directory should be deleted.
     */
    fun deleteSubjectDirectory(subject: Subject) {
        val subjectFileDir = File(Uri.parse(subject.directoryUriString).path!!)
        subjectFileDir.deleteRecursively()
    }
}
