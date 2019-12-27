package com.example.audiolearning.audio.audio_store

import android.content.Context
import android.net.Uri
import com.example.audiolearning.models.Audio
import com.example.audiolearning.models.Subject

class AudioStore(context: Context): IAudioStore{
    private val resolver = context.contentResolver

    override fun save(audio: Audio) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(audioUri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bulkGetBySubject(subject: Subject) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun get(audioUri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(audioUri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}