package com.example.audiolearning.data.repositories

import com.example.audiolearning.data.db.AudioLearningDatabase
import com.example.audiolearning.data.db.entities.Audio

class AudioRepository(private val db: AudioLearningDatabase) {
    suspend fun upsert(audio: Audio) = db.getAudioDao().upsert(audio)

    suspend fun delete(audio: Audio) = db.getAudioDao().delete(audio)
}