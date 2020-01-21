package com.example.audiolearning.data.repositories

import com.example.audiolearning.data.db.AudioLearningDatabase
import com.example.audiolearning.data.db.entities.Subject

class SubjectRepository(private val db: AudioLearningDatabase) {
    suspend fun upsert(subject: Subject) = db.getSubjectDao().upsert(subject)

    suspend fun delete(subject: Subject) = db.getSubjectDao().delete(subject)

    suspend fun getAllSubjects() = db.getSubjectDao().getAllSubjects()

    suspend fun getSubjectByName(name: String) = db.getSubjectDao().getSubjectByName(name)
}