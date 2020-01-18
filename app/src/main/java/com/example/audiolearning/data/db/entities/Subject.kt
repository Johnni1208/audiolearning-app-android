package com.example.audiolearning.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class Subject(
    @ColumnInfo(name = "subject_name")
    val name: String,

    @ColumnInfo(name = "subject_directory_uri")
    val directoryUriString: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "subject_id")
    var id: Long? = null

    @Ignore
    var isRealSubject: Boolean = true
}