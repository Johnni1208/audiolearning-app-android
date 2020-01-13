package com.example.audiolearning.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.audiolearning.data.db.entities.Audio

@Dao
interface AudioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(audio: Audio)

    @Delete
    suspend fun delete(audio: Audio)
}