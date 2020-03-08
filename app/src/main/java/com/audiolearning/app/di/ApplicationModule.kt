package com.audiolearning.app.di

import com.audiolearning.app.audio.audio_recorder.AudioRecorder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ApplicationModule {
    @JvmStatic
    @Singleton
    @Provides
    fun provideAudioRecorder() = AudioRecorder(null)
}