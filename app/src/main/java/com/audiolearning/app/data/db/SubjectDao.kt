package com.audiolearning.app.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.audiolearning.app.data.db.entities.Subject

@Dao
interface SubjectDao {
    @Insert
    suspend fun insert(subject: Subject)

    @Query("SELECT * FROM subjects")
    fun getAllSubjects(): LiveData<List<Subject>>

    @Delete
    suspend fun delete(subject: Subject)

    @Query("SELECT * FROM subjects where subject_name = :name")
    suspend fun getSubjectByName(name: String): Subject?
}