package com.example.audiolearning.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "audios")
data class Audio(
    @ColumnInfo(name = "audio_name")
    val name: String,

    @ColumnInfo(name = "audio_subject_id")
    val subjectId: Long,

    @ColumnInfo(name = "audio_file_uri")
    val fileUriString: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "audio_id")
    var id: Long? = null
}