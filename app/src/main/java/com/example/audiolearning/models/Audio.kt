package com.example.audiolearning.models

import java.io.File

data class Audio(
    val file: File,
    val name: String,
    val subject: Subject
)