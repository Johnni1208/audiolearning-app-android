package com.audiolearning.app.di.ui

import androidx.lifecycle.ViewModel
import com.audiolearning.app.di.ViewModelBuilder
import com.audiolearning.app.di.ViewModelKey
import com.audiolearning.app.ui.fragments.subjects.SubjectsFragment
import com.audiolearning.app.ui.fragments.subjects.SubjectsFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class SubjectsFragmentModule {
    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun subjectsFragment(): SubjectsFragment

    @Binds
    @IntoMap
    @ViewModelKey(SubjectsFragmentViewModel::class)
    abstract fun bindViewModel(fragmentViewModel: SubjectsFragmentViewModel): ViewModel
}