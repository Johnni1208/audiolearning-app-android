package com.audiolearning.app.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.audiolearning.app.data.db.entities.Audio

@Dao
interface AudioDao {
    @Insert
    suspend fun insert(audio: Audio)

    @Delete
    suspend fun delete(audio: Audio)

    @Query("SELECT * FROM audios")
    suspend fun getAllAudios(): List<Audio>

    @Query("SELECT * FROM audios WHERE audio_id = :id")
    suspend fun getAudioById(id: Int): Audio?

    @Query("SELECT * FROM audios WHERE audio_name = :name")
    suspend fun getAudioByName(name: String): Audio?

    @Query("SELECT * FROM audios WHERE audio_subject_id = :subjectId")
    suspend fun getAudiosOfSubject(subjectId: Int): List<Audio>

    @Query("SELECT * FROM audios WHERE audio_subject_id = :subjectId")
    fun getAudiosOfSubjectLiveData(subjectId: Int): LiveData<List<Audio>>
}
