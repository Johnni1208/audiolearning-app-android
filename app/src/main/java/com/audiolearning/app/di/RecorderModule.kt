package com.audiolearning.app.di

import androidx.lifecycle.ViewModel
import com.audiolearning.app.ui.fragments.recorder.RecorderFragment
import com.audiolearning.app.ui.fragments.recorder.RecorderViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class RecorderModule {
    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun recorderFragment(): RecorderFragment

    @Binds
    @IntoMap
    @ViewModelKey(RecorderViewModel::class)
    abstract fun bindViewModel(viewModel: RecorderViewModel): ViewModel
}