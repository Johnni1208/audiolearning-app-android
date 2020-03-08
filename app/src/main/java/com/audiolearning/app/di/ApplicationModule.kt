package com.audiolearning.app.di

import android.content.Context
import com.audiolearning.app.audio.audio_recorder.AudioRecorder
import com.audiolearning.app.data.db.AudioLearningDatabase
import com.audiolearning.app.data.repositories.AudioRepository
import com.audiolearning.app.data.repositories.SubjectRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ApplicationModule {
    @JvmStatic
    @Singleton
    @Provides
    fun provideAudioRecorder() = AudioRecorder(null)

    @JvmStatic
    @Singleton
    @Provides
    fun provideDatabase(context: Context) = AudioLearningDatabase.invoke(context)

    @Provides
    fun provideAudioRepository(db: AudioLearningDatabase) = AudioRepository(db)

    @Provides
    fun provideSubjectRepository(db: AudioLearningDatabase, context: Context) =
        SubjectRepository(db, context.filesDir)
}