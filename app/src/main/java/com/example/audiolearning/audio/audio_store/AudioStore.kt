package com.example.audiolearning.audio.audio_store

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.audiolearning.models.Audio
import com.example.audiolearning.models.Subject
import com.example.audiolearning.util.AudioFileUtils

class AudioStore(var context: Context) : IAudioStore {
    override fun save(audio: Audio) {
        AudioFileUtils.moveFile(audio.file, audio.subject.directory.absolutePath, audio.name)
        Log.i("AudioStore", audio.file.absolutePath)
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