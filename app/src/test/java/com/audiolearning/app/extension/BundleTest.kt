package com.audiolearning.app.extension

import android.os.Bundle
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.data.db.entities.Subject
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BundleTest {
    private val subject = Subject("testSubject", "").apply { id = 0 }
    private val audio = Audio("testAudio", "", 69, subject.id!!).apply { id = 0 }
    private val bundle = Bundle().from(audio, subject)

    @Test
    fun bundleFrom_ShouldReturnCorrectBundle() {
        assertEquals(audio.id.toString(), bundle.getString(KEY_AUDIO_ID))
        assertEquals(audio.name, bundle.getString(KEY_AUDIO_NAME))
        assertEquals(audio.fileUriString, bundle.getString(KEY_AUDIO_URI))
        assertEquals(audio.durationInMilliseconds, bundle.getLong(KEY_AUDIO_DURATION))
        assertEquals(audio.createDate, bundle.getLong(KEY_AUDIO_DATE))
        assertEquals(subject.name, bundle.getString(KEY_SUBJECT_NAME))
    }

    @Test
    fun bundleAudioId_ShouldReturnCorrectId() {
        assertEquals(bundle.audioId, audio.id.toString())
    }

    @Test
    fun bundleAudioName_ShouldReturnCorrectName() {
        assertEquals(bundle.audioName, audio.name)
    }

    @Test
    fun bundleAudioUri_ShouldReturnCorrectFileUri() {
        assertEquals(bundle.audioUri, audio.fileUriString)
    }

    @Test
    fun bundleAudioDuration_ShouldReturnCorrectDuration() {
        assertEquals(bundle.audioDuration, audio.durationInMilliseconds)
    }

    @Test
    fun bundleAudioDate_ShouldReturnCorrectDate() {
        assertEquals(bundle.audioDate, audio.createDate)
    }

    @Test
    fun bundleSubjectName_ShouldReturnCorrectName() {
        assertEquals(bundle.subjectName, subject.name)
    }
}
