package com.example.audiolearning.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.audiolearning.data.db.entities.Audio

@Dao
interface AudioDao {
    @Insert
    suspend fun insert(audio: Audio)

    @Query("SELECT * FROM audios")
    suspend fun getAllAudios(): List<Audio>

    @Query("SELECT * FROM audios WHERE audio_name = :name")
    suspend fun getAudioByName(name: String): Audio?
}