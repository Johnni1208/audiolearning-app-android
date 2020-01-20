package com.example.audiolearning.data.db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class SubjectWithAudios(
    @Embedded val subject: Subject,
    @Relation(
        parentColumn = "subject_id",
        entityColumn = "audio_subject_id"
    )
    val audios: List<Audio>
)