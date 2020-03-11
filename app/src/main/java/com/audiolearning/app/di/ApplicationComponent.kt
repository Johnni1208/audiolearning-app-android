package com.audiolearning.app.di

import android.content.Context
import com.audiolearning.app.AudioLearningApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ApplicationModule::class,
        RecorderModule::class,
        SubjectsModule::class,
        NewRecordingDialogModule::class,
        CreateNewSubjectDialogModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<AudioLearningApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}