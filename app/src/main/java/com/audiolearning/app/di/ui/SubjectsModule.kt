package com.audiolearning.app.di.ui

import androidx.lifecycle.ViewModel
import com.audiolearning.app.di.ViewModelBuilder
import com.audiolearning.app.di.ViewModelKey
import com.audiolearning.app.ui.fragments.subjects.SubjectsFragment
import com.audiolearning.app.ui.fragments.subjects.SubjectsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class SubjectsModule {
    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun subjectsFragment(): SubjectsFragment

    @Binds
    @IntoMap
    @ViewModelKey(SubjectsViewModel::class)
    abstract fun bindViewModel(viewModel: SubjectsViewModel): ViewModel
}