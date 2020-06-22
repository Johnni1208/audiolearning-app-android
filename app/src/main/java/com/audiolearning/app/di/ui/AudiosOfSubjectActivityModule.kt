package com.audiolearning.app.di.ui

import androidx.lifecycle.ViewModel
import com.audiolearning.app.di.ViewModelBuilder
import com.audiolearning.app.di.ViewModelKey
import com.audiolearning.app.ui.activity.audiosofsubject.AudiosOfSubjectActivity
import com.audiolearning.app.ui.activity.audiosofsubject.AudiosOfSubjectActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class AudiosOfSubjectActivityModule {
    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun audiosOfSubjectActivity(): AudiosOfSubjectActivity

    @Binds
    @IntoMap
    @ViewModelKey(AudiosOfSubjectActivityViewModel::class)
    abstract fun bindViewModel(activityViewModel: AudiosOfSubjectActivityViewModel): ViewModel
}
