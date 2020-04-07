package com.audiolearning.app.di.ui

import androidx.lifecycle.ViewModel
import com.audiolearning.app.di.ViewModelBuilder
import com.audiolearning.app.di.ViewModelKey
import com.audiolearning.app.ui.fragments.recorder.RecorderFragment
import com.audiolearning.app.ui.fragments.recorder.RecorderFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class RecorderFragmentModule {
    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun recorderFragment(): RecorderFragment

    @Binds
    @IntoMap
    @ViewModelKey(RecorderFragmentViewModel::class)
    abstract fun bindViewModel(fragmentViewModel: RecorderFragmentViewModel): ViewModel
}