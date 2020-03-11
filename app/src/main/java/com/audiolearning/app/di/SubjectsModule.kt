package com.audiolearning.app.di

import androidx.lifecycle.ViewModel
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