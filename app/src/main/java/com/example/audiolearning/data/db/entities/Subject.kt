package com.example.audiolearning.data.db.entities

import androidx.room.PrimaryKey
import java.io.File

data class Subject(
    val name: String,
    val directory: File
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}