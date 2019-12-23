package com.example.audiolearning.audio.audio_store

interface IAudioStore {
    fun save()
    fun delete()
    fun get()
    fun update()
}