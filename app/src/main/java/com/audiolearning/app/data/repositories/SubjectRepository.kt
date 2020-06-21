package com.audiolearning.app.data.repositories

import androidx.core.net.toUri
import com.audiolearning.app.data.db.AudioLearningDatabase
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.util.file.SubjectFileUtils
import java.io.File
import javax.inject.Inject

class SubjectRepository @Inject constructor(
    private val db: AudioLearningDatabase,
    private val filesDir: File
) {
    suspend fun insert(subjectName: String) {
        val subjectDir = SubjectFileUtils.createNewSubjectDirectory(filesDir, subjectName)

        db.getSubjectDao().insert(Subject(subjectName, subjectDir.toUri().toString()))
    }

    suspend fun delete(subject: Subject) {
        SubjectFileUtils.deleteSubjectDirectory(subject)
        db.getSubjectDao().delete(subject)
    }

    fun getAllSubjects() = db.getSubjectDao().getAllSubjects()

    suspend fun getSubjectByName(name: String) = db.getSubjectDao().getSubjectByName(name)

    suspend fun getSubjectById(id: Int) = db.getSubjectDao().getSubjectById(id)
}
