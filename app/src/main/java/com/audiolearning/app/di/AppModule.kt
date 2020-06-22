package com.audiolearning.app.di

import android.content.Context
import com.audiolearning.app.audio.recorder.AudioRecorder
import com.audiolearning.app.data.db.AudioLearningDatabase
import com.audiolearning.app.data.db.entities.Audio
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.data.store.SelectedEntityStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Provides
    fun provideAudioRecorder() = AudioRecorder(null)

    @Provides
    fun provideSelectedSubjectStore() =
        SelectedEntityStore<Subject>()

    @Provides
    fun provideSelectedAudioStore() =
        SelectedEntityStore<Audio>()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        AudioLearningDatabase.invoke(context)

    @Singleton
    @Provides
    fun provideFilesDir(@ApplicationContext context: Context): File = context.filesDir
}