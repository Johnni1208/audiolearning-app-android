package com.example.audiolearning.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.audiolearning.data.db.entities.Subject

@Dao
interface SubjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(subject: Subject)

    @Query("SELECT * FROM subjects")
    fun getAllSubjects(): LiveData<List<Subject>>

    @Delete
    suspend fun delete(subject: Subject)

    @Query("SELECT * FROM subjects where subject_name = :name")
    suspend fun getSubjectByName(name: String): Subject?
}