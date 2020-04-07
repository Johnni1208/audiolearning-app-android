package com.audiolearning.app.di.ui

import androidx.lifecycle.ViewModel
import com.audiolearning.app.di.ViewModelBuilder
import com.audiolearning.app.di.ViewModelKey
import com.audiolearning.app.ui.activities.subject.SubjectActivity
import com.audiolearning.app.ui.activities.subject.SubjectActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class SubjectActivityModule {
    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun subjectActivity(): SubjectActivity

    @Binds
    @IntoMap
    @ViewModelKey(SubjectActivityViewModel::class)
    abstract fun bindViewModel(fragmentViewModel: SubjectActivityViewModel): ViewModel
}