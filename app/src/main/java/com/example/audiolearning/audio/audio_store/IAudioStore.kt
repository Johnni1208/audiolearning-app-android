package com.example.audiolearning.audio.audio_store

import android.net.Uri
import com.example.audiolearning.models.Audio
import com.example.audiolearning.models.Subject

interface IAudioStore {
    fun save(audio: Audio)
    fun delete(audioUri: Uri)
    fun get(audioUri: Uri)
    fun bulkGetBySubject(subject: Subject)
    fun update(audioUri: Uri)
}