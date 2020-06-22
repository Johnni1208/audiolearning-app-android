package com.audiolearning.app.di

import android.content.Context
import com.audiolearning.app.AudioLearningApplication
import com.audiolearning.app.di.ui.AudiosOfSubjectActivityModule
import com.audiolearning.app.di.ui.CreateNewSubjectDialogModule
import com.audiolearning.app.di.ui.NewRecordingDialogModule
import com.audiolearning.app.di.ui.RecorderFragmentModule
import com.audiolearning.app.di.ui.SubjectsFragmentModule
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
        RecorderFragmentModule::class,
        SubjectsFragmentModule::class,
        NewRecordingDialogModule::class,
        CreateNewSubjectDialogModule::class,
        AudiosOfSubjectActivityModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<AudioLearningApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}
