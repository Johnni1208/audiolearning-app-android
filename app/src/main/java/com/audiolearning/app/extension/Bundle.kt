package com.audiolearning.app.extension

import android.os.Bundle
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.data.db.entities.Subject

const val KEY_AUDIO_ID = "bundleKeyAudioId"
const val KEY_AUDIO_NAME = "bundleKeyAudioName"
const val KEY_AUDIO_URI = "bundleKeyAudioUri"
const val KEY_AUDIO_DURATION = "bundleKeyAudioDuration"
const val KEY_AUDIO_DATE = "bundleKeyAudioDate"
const val KEY_SUBJECT_NAME = "bundleKeySubjectName"

/**
 * Create a bundle from an [Audio] and a [Subject].
 */
fun Bundle.from(audio: Audio, subject: Subject): Bundle {
    putString(KEY_AUDIO_ID, audio.id.toString())
    putString(KEY_AUDIO_NAME, audio.name)
    putString(KEY_AUDIO_URI, audio.fileUriString)
    putLong(KEY_AUDIO_DURATION, audio.durationInMilliseconds)
    putString(KEY_AUDIO_DATE, audio.createDate.toFormattedDate())
    putString(KEY_SUBJECT_NAME, subject.name)
    return this
}

inline val Bundle.audioId
    get() = this.getString(KEY_AUDIO_ID) ?: ""

inline val Bundle.audioName
    get() = this.getString(KEY_AUDIO_NAME) ?: ""

inline val Bundle.audioUri
    get() = this.getString(KEY_AUDIO_URI) ?: ""

inline val Bundle.audioDuration
    get() = this.getLong(KEY_AUDIO_DURATION)

inline val Bundle.audioDate
    get() = this.getString(KEY_AUDIO_DATE)

inline val Bundle.subjectName
    get() = this.getString(KEY_SUBJECT_NAME) ?: ""
