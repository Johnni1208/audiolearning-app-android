package com.example.audiolearning.data.repositories

import android.net.Uri
import com.example.audiolearning.data.db.AudioLearningDatabase
import com.example.audiolearning.data.db.entities.Subject
import com.example.audiolearning.util.SubjectFileUtils
import java.io.File

class SubjectRepository(
    private val db: AudioLearningDatabase,
    private val filesDir: File
) {
    suspend fun insert(subjectName: String) {
        val subjectDir = SubjectFileUtils.createNewSubjectDirectory(filesDir, subjectName)

        db.getSubjectDao().insert(Subject(subjectName, Uri.fromFile(subjectDir).toString()))
    }

    suspend fun delete(subject: Subject) = db.getSubjectDao().delete(subject)

    fun getAllSubjects() = db.getSubjectDao().getAllSubjects()

    suspend fun getSubjectByName(name: String) = db.getSubjectDao().getSubjectByName(name)
}
